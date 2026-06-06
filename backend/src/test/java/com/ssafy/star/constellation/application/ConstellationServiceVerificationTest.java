package com.ssafy.star.constellation.application;

import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.exception.ByeolDamException;
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
import com.ssafy.star.support.TestContainerSupport;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ConstellationServiceVerificationTest extends TestContainerSupport {

    @Autowired
    ConstellationService constellationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    ConstellationRepository constellationRepository;
    @Autowired
    ConstellationUserRepository constellationUserRepository;
    @Autowired
    ContourRepository contourRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ImageRepository imageRepository;

    @Test
    void myConstellations_윤곽선과_hover_게시물을_함께_조회한다() {
        UserEntity owner = saveUser(DisclosureType.VISIBLE);
        ConstellationEntity constellation = saveConstellation(owner, "ORION");
        saveArticle("star", owner, constellation, DisclosureType.VISIBLE);

        var result = constellationService.myConstellations(owner.getEmail(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("ORION");
        assertThat(result.getContent().get(0).contour().originUrl()).startsWith("test://contour/origin");
        assertThat(result.getContent().get(0).hoverArticles()).hasSize(1);
        assertThat(result.getContent().get(0).hoverArticles().get(0).articleThumbnail()).startsWith("test://image/star-thumb");
    }

    @Test
    void myConstellations_페이지_조회도_윤곽선과_hover_게시물을_함께_조회한다() {
        UserEntity owner = saveUser(DisclosureType.VISIBLE);
        ConstellationEntity constellation = saveConstellation(owner, "ORION-PAGE");
        saveArticle("star-page", owner, constellation, DisclosureType.VISIBLE);

        var result = constellationService.myConstellations(owner.getEmail(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("ORION-PAGE");
        assertThat(result.getContent().get(0).contour().originUrl()).startsWith("test://contour/origin");
        assertThat(result.getContent().get(0).constellationUsers()).hasSize(1);
        assertThat(result.getContent().get(0).hoverArticles()).hasSize(1);
        assertThat(result.getContent().get(0).hoverArticles().get(0).articleThumbnail()).startsWith("test://image/star-page-thumb");
    }

    @Test
    void userConstellations_비공개_사용자는_팔로우_승인_없으면_조회할_수_없다() {
        UserEntity owner = saveUser(DisclosureType.INVISIBLE);
        UserEntity viewer = saveUser(DisclosureType.VISIBLE);
        saveConstellation(owner, "PRIVATE");

        assertThatThrownBy(() -> constellationService.userConstellations(owner.getNickname(), viewer.getEmail(), PageRequest.of(0, 10)))
                .isInstanceOf(ByeolDamException.class);
    }

    @Test
    void userConstellations_비공개_사용자라도_팔로우_승인되면_조회한다() {
        UserEntity owner = saveUser(DisclosureType.INVISIBLE);
        UserEntity viewer = saveUser(DisclosureType.VISIBLE);
        followRepository.save(FollowEntity.of(viewer, owner, LocalDateTime.now(), ApprovalStatus.ACCEPT));
        saveConstellation(owner, "FOLLOWED");

        var result = constellationService.userConstellations(owner.getNickname(), viewer.getEmail(), PageRequest.of(0, 10));

        assertThat(result.getContent()).extracting("name")
                .contains("FOLLOWED");
    }

    @Test
    void userConstellations_페이지_조회도_팔로우_승인되면_조회한다() {
        UserEntity owner = saveUser(DisclosureType.INVISIBLE);
        UserEntity viewer = saveUser(DisclosureType.VISIBLE);
        followRepository.save(FollowEntity.of(viewer, owner, LocalDateTime.now(), ApprovalStatus.ACCEPT));
        saveConstellation(owner, "FOLLOWED-PAGE");

        var result = constellationService.userConstellations(owner.getNickname(), viewer.getEmail(), PageRequest.of(0, 10));

        assertThat(result.getContent()).extracting("name")
                .contains("FOLLOWED-PAGE");
    }

    private UserEntity saveUser(DisclosureType disclosureType) {
        String suffix = UUID.randomUUID().toString().replace("-", "");
        UserEntity user = UserEntity.of(
                "user-" + suffix + "@example.com",
                ProviderType.LOCAL,
                "password",
                "테스트",
                "nick" + suffix.substring(0, 12)
        );
        user.setDisclosureType(disclosureType);
        return userRepository.save(user);
    }

    private ConstellationEntity saveConstellation(UserEntity admin, String name) {
        ContourEntity contour = contourRepository.save(
                ContourEntity.of(
                        fakeContourUrl("origin"),
                        fakeContourUrl("thumb"),
                        fakeContourUrl("cthumb"),
                        List.of(List.of(List.of(1, 2))),
                        List.of(List.of(1, 2))
                )
        );
        ConstellationEntity constellation = ConstellationEntity.of(name);
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
                fakeImageUrl(title),
                fakeImageUrl(title + "-thumb"),
                ImageType.ARTICLE
        ));
        return articleRepository.saveAndFlush(
                ArticleEntity.of(title, "description", disclosureType, owner, constellation, image)
        );
    }

    private static String fakeContourUrl(String name) {
        return "test://contour/" + name + "/" + UUID.randomUUID();
    }

    private static String fakeImageUrl(String name) {
        return "test://image/" + name + "/" + UUID.randomUUID();
    }
}
