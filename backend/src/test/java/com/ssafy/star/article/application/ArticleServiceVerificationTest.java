package com.ssafy.star.article.application;

import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.repository.ContourRepository;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.dao.ImageRepository;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceVerificationTest {

    @Autowired
    ArticleService articleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ConstellationRepository constellationRepository;
    @Autowired
    ConstellationUserRepository constellationUserRepository;
    @Autowired
    ContourRepository contourRepository;

    @Test
    void userArticleList_본인은_비공개_게시물까지_조회한다() {
        UserEntity owner = saveUser();
        saveArticle("visible", owner, null, DisclosureType.VISIBLE);
        saveArticle("invisible", owner, null, DisclosureType.INVISIBLE);

        var result = articleService.userArticleList(owner.getNickname(), owner.getEmail());

        assertThat(result).extracting("title")
                .contains("visible", "invisible");
    }

    @Test
    void userArticleList_팔로우하지_않은_사용자는_공개_게시물만_조회한다() {
        UserEntity owner = saveUser();
        UserEntity viewer = saveUser();
        saveArticle("visible", owner, null, DisclosureType.VISIBLE);
        saveArticle("invisible", owner, null, DisclosureType.INVISIBLE);

        var result = articleService.userArticleList(owner.getNickname(), viewer.getEmail());

        assertThat(result).extracting("title")
                .containsExactly("visible");
    }

    @Test
    void followFeed_팔로우_승인된_사용자의_게시물을_최신순으로_페이징한다() {
        UserEntity viewer = saveUser();
        UserEntity followed = saveUser();
        UserEntity notFollowed = saveUser();
        followRepository.save(FollowEntity.of(viewer, followed, LocalDateTime.now(), ApprovalStatus.ACCEPT));
        saveArticle("old", followed, null, DisclosureType.VISIBLE);
        saveArticle("new", followed, null, DisclosureType.VISIBLE);
        saveArticle("excluded", notFollowed, null, DisclosureType.VISIBLE);

        var result = articleService.followFeed(viewer.getEmail(), PageRequest.of(0, 10));

        assertThat(result.getContent()).extracting("title")
                .containsExactly("new", "old");
    }

    @Test
    void articlesInConstellation_작성자는_별자리의_비공개_게시물도_조회한다() {
        UserEntity owner = saveUser();
        ConstellationEntity constellation = saveConstellation(owner);
        saveArticle("visible", owner, constellation, DisclosureType.VISIBLE);
        saveArticle("invisible", owner, constellation, DisclosureType.INVISIBLE);

        var result = articleService.articlesInConstellation(constellation.getId(), owner.getEmail());

        assertThat(result).extracting("title")
                .contains("visible", "invisible");
    }

    @Test
    void delete_삭제된_게시물은_일반목록에서_빠지고_휴지통에서_조회된다() {
        UserEntity owner = saveUser();
        ArticleEntity article = saveArticle("deleted", owner, null, DisclosureType.VISIBLE);

        articleService.delete(article.getId(), owner.getEmail());

        assertThat(articleService.userArticleList(owner.getNickname(), owner.getEmail()))
                .extracting("title")
                .doesNotContain("deleted");
        assertThat(articleService.trashcan(owner.getEmail(), PageRequest.of(0, 10)).getContent())
                .extracting("title")
                .contains("deleted");
    }

    private UserEntity saveUser() {
        String suffix = UUID.randomUUID().toString().replace("-", "");
        return userRepository.save(UserEntity.of(
                "user-" + suffix + "@example.com",
                ProviderType.LOCAL,
                "password",
                "테스트",
                "nick" + suffix.substring(0, 12)
        ));
    }

    private ConstellationEntity saveConstellation(UserEntity admin) {
        ContourEntity contour = contourRepository.save(
                ContourEntity.of(fakeUrl("origin"), fakeUrl("thumb"), fakeUrl("cthumb"), List.of(), List.of())
        );
        ConstellationEntity constellation = ConstellationEntity.of("ORION-" + UUID.randomUUID());
        constellation.setContourId(contour.get_id());
        constellationRepository.saveAndFlush(constellation);
        constellationUserRepository.saveAndFlush(
                ConstellationUserEntity.of(constellation, admin, ConstellationUserRole.ADMIN)
        );
        return constellation;
    }

    private ArticleEntity saveArticle(
            String title,
            UserEntity owner,
            ConstellationEntity constellation,
            DisclosureType disclosureType
    ) {
        ImageEntity image = imageRepository.save(ImageEntity.of(
                title + ".png",
                fakeUrl(title),
                fakeUrl(title + "-thumb"),
                ImageType.ARTICLE
        ));
        ArticleEntity article = ArticleEntity.of(
                title,
                "description",
                disclosureType,
                owner,
                constellation,
                image
        );
        return articleRepository.saveAndFlush(article);
    }

    private static String fakeUrl(String name) {
        return "test://image/" + name + "/" + UUID.randomUUID();
    }
}
