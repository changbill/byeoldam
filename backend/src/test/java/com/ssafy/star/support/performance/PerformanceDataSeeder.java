package com.ssafy.star.support.performance;

import com.ssafy.star.article.dao.ArticleLikeRepository;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.repository.CommentRepository;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.dao.ConstellationLikeRepository;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
class PerformanceDataSeeder {

    private static final String USER_EMAIL_PREFIX = "perf-user-";
    private static final String USER_NICKNAME_PREFIX = "perfuser";
    private static final String ARTICLE_TITLE_PREFIX = "PERF-ARTICLE-";
    private static final String CONSTELLATION_NAME_PREFIX = "PERF-CONSTELLATION-";
    private static final String PERF_URL_PREFIX = "test://perf/";

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final ImageRepository imageRepository;
    private final ArticleRepository articleRepository;
    private final ConstellationRepository constellationRepository;
    private final ConstellationUserRepository constellationUserRepository;
    private final ContourRepository contourRepository;
    private final CommentRepository commentRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final ConstellationLikeRepository constellationLikeRepository;
    private final MongoTemplate mongoTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    PerformanceDataSeeder(
            UserRepository userRepository,
            FollowRepository followRepository,
            ImageRepository imageRepository,
            ArticleRepository articleRepository,
            ConstellationRepository constellationRepository,
            ConstellationUserRepository constellationUserRepository,
            ContourRepository contourRepository,
            CommentRepository commentRepository,
            ArticleLikeRepository articleLikeRepository,
            ConstellationLikeRepository constellationLikeRepository,
            MongoTemplate mongoTemplate
    ) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.imageRepository = imageRepository;
        this.articleRepository = articleRepository;
        this.constellationRepository = constellationRepository;
        this.constellationUserRepository = constellationUserRepository;
        this.contourRepository = contourRepository;
        this.commentRepository = commentRepository;
        this.articleLikeRepository = articleLikeRepository;
        this.constellationLikeRepository = constellationLikeRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional
    PerformanceSeedResult recreate(PerformanceDataset dataset) {
        deleteExistingPerfData();

        List<UserEntity> users = createUsers(dataset);
        List<FollowEntity> follows = createFollows(dataset, users);
        List<ConstellationEntity> constellations = createConstellations(dataset, users);
        List<ArticleEntity> articles = createArticles(dataset, users, constellations);
        List<CommentEntity> comments = createComments(dataset, users, articles);
        List<ArticleLikeEntity> articleLikes = createArticleLikes(dataset, users, articles);
        List<ConstellationLikeEntity> constellationLikes = createConstellationLikes(dataset, users, constellations);

        entityManager.flush();
        entityManager.clear();

        return new PerformanceSeedResult(
                dataset,
                users.size(),
                follows.size(),
                articles.size(),
                constellations.size(),
                comments.size(),
                articleLikes.size(),
                constellationLikes.size(),
                users.get(0).getEmail(),
                users.get(1).getNickname(),
                users.get(2 % users.size()).getNickname()
        );
    }

    private void deleteExistingPerfData() {
        entityManager.createQuery("""
                DELETE FROM ArticleLikeEntity entity
                WHERE entity.articleEntity.title LIKE :articlePrefix
                   OR entity.userEntity.email LIKE :emailPrefix
                """)
                .setParameter("articlePrefix", ARTICLE_TITLE_PREFIX + "%")
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM ConstellationLikeEntity entity
                WHERE entity.constellationEntity.name LIKE :constellationPrefix
                   OR entity.userEntity.email LIKE :emailPrefix
                """)
                .setParameter("constellationPrefix", CONSTELLATION_NAME_PREFIX + "%")
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM CommentEntity entity
                WHERE entity.articleEntity.title LIKE :articlePrefix
                   OR entity.userEntity.email LIKE :emailPrefix
                """)
                .setParameter("articlePrefix", ARTICLE_TITLE_PREFIX + "%")
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM FollowEntity entity
                WHERE entity.fromUser.email LIKE :emailPrefix
                   OR entity.toUser.email LIKE :emailPrefix
                """)
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM ArticleEntity entity
                WHERE entity.title LIKE :articlePrefix
                   OR entity.ownerEntity.email LIKE :emailPrefix
                """)
                .setParameter("articlePrefix", ARTICLE_TITLE_PREFIX + "%")
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM ConstellationUserEntity entity
                WHERE entity.constellationEntity.name LIKE :constellationPrefix
                   OR entity.userEntity.email LIKE :emailPrefix
                """)
                .setParameter("constellationPrefix", CONSTELLATION_NAME_PREFIX + "%")
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM ConstellationEntity entity
                WHERE entity.name LIKE :constellationPrefix
                """)
                .setParameter("constellationPrefix", CONSTELLATION_NAME_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM UserEntity entity
                WHERE entity.email LIKE :emailPrefix
                   OR entity.nickname LIKE :nicknamePrefix
                """)
                .setParameter("emailPrefix", USER_EMAIL_PREFIX + "%")
                .setParameter("nicknamePrefix", USER_NICKNAME_PREFIX + "%")
                .executeUpdate();
        entityManager.createQuery("""
                DELETE FROM ImageEntity entity
                WHERE entity.url LIKE :urlPrefix
                   OR entity.thumbnailUrl LIKE :urlPrefix
                """)
                .setParameter("urlPrefix", PERF_URL_PREFIX + "%")
                .executeUpdate();
        mongoTemplate.remove(
                Query.query(Criteria.where("originUrl").regex("^" + PERF_URL_PREFIX)),
                ContourEntity.class
        );
        entityManager.flush();
        entityManager.clear();
    }

    private List<UserEntity> createUsers(PerformanceDataset dataset) {
        List<UserEntity> users = new ArrayList<>(dataset.userCount());
        for (int index = 0; index < dataset.userCount(); index++) {
            UserEntity user = UserEntity.of(
                    USER_EMAIL_PREFIX + index + "@example.com",
                    ProviderType.LOCAL,
                    "password",
                    "성능사용자" + index,
                    USER_NICKNAME_PREFIX + index
            );
            if (index % 10 == 0) {
                user.setDisclosureType(DisclosureType.INVISIBLE);
            }
            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    private List<FollowEntity> createFollows(PerformanceDataset dataset, List<UserEntity> users) {
        List<FollowEntity> follows = new ArrayList<>(dataset.followCount());
        UserEntity baselineUser = users.get(0);
        int focusedFollowCount = Math.min(dataset.focusedFollowCount(), users.size() - 1);
        for (int index = 1; index <= focusedFollowCount; index++) {
            follows.add(FollowEntity.of(baselineUser, users.get(index), LocalDateTime.now(), ApprovalStatus.ACCEPT));
        }

        int fromIndex = 1;
        int toIndex = 2;
        while (follows.size() < dataset.followCount()) {
            UserEntity fromUser = users.get(fromIndex % users.size());
            UserEntity toUser = users.get(toIndex % users.size());
            if (!fromUser.equals(toUser)) {
                ApprovalStatus status = follows.size() % 10 == 0 ? ApprovalStatus.REQUEST : ApprovalStatus.ACCEPT;
                follows.add(FollowEntity.of(fromUser, toUser, LocalDateTime.now(), status));
            }
            fromIndex++;
            toIndex += 7;
        }
        return followRepository.saveAll(follows);
    }

    private List<ConstellationEntity> createConstellations(PerformanceDataset dataset, List<UserEntity> users) {
        List<ConstellationEntity> constellations = new ArrayList<>(dataset.constellationCount());
        UserEntity heavyOwner = users.get(2 % users.size());
        for (int index = 0; index < dataset.constellationCount(); index++) {
            UserEntity owner = index < dataset.focusedConstellationCount()
                    ? heavyOwner
                    : users.get(index % users.size());
            ContourEntity contour = contourRepository.save(ContourEntity.of(
                    PERF_URL_PREFIX + "contour/origin/" + index,
                    PERF_URL_PREFIX + "contour/thumb/" + index,
                    PERF_URL_PREFIX + "contour/cthumb/" + index,
                    List.of(List.of(List.of(index, index + 1))),
                    List.of(List.of(index, index + 1))
            ));
            ConstellationEntity constellation = ConstellationEntity.of(CONSTELLATION_NAME_PREFIX + index);
            constellation.setContourId(contour.get_id());
            constellations.add(constellationRepository.save(constellation));
            constellationUserRepository.save(ConstellationUserEntity.of(
                    constellation,
                    owner,
                    ConstellationUserRole.ADMIN
            ));
        }
        return constellations;
    }

    private List<ArticleEntity> createArticles(
            PerformanceDataset dataset,
            List<UserEntity> users,
            List<ConstellationEntity> constellations
    ) {
        List<ArticleEntity> articles = new ArrayList<>(dataset.articleCount());
        UserEntity heavyOwner = users.get(1);
        for (int index = 0; index < dataset.articleCount(); index++) {
            UserEntity owner = index < dataset.focusedArticleCount()
                    ? heavyOwner
                    : users.get(index % users.size());
            ConstellationEntity constellation = index % 20 == 0
                    ? null
                    : constellations.get(index % constellations.size());
            ImageEntity image = imageRepository.save(ImageEntity.of(
                    "perf-article-" + index + ".png",
                    PERF_URL_PREFIX + "article/origin/" + index,
                    PERF_URL_PREFIX + "article/thumb/" + index,
                    ImageType.ARTICLE
            ));
            DisclosureType disclosureType = index % 7 == 0
                    ? DisclosureType.INVISIBLE
                    : DisclosureType.VISIBLE;
            articles.add(articleRepository.save(ArticleEntity.of(
                    ARTICLE_TITLE_PREFIX + index,
                    "performance description " + index,
                    disclosureType,
                    owner,
                    constellation,
                    image
            )));
        }
        return articles;
    }

    private List<CommentEntity> createComments(
            PerformanceDataset dataset,
            List<UserEntity> users,
            List<ArticleEntity> articles
    ) {
        List<CommentEntity> comments = new ArrayList<>(dataset.commentCount());
        for (int index = 0; index < dataset.commentCount(); index++) {
            comments.add(CommentEntity.of(
                    users.get(index % users.size()),
                    articles.get(index % articles.size()),
                    "performance comment " + index,
                    null
            ));
        }
        return commentRepository.saveAll(comments);
    }

    private List<ArticleLikeEntity> createArticleLikes(
            PerformanceDataset dataset,
            List<UserEntity> users,
            List<ArticleEntity> articles
    ) {
        List<ArticleLikeEntity> articleLikes = new ArrayList<>(dataset.articleLikeCount());
        for (int index = 0; index < dataset.articleLikeCount(); index++) {
            articleLikes.add(ArticleLikeEntity.of(
                    users.get((index * 7) % users.size()),
                    articles.get(index % articles.size())
            ));
        }
        return articleLikeRepository.saveAll(articleLikes);
    }

    private List<ConstellationLikeEntity> createConstellationLikes(
            PerformanceDataset dataset,
            List<UserEntity> users,
            List<ConstellationEntity> constellations
    ) {
        List<ConstellationLikeEntity> constellationLikes = new ArrayList<>(dataset.constellationLikeCount());
        for (int index = 0; index < dataset.constellationLikeCount(); index++) {
            constellationLikes.add(ConstellationLikeEntity.of(
                    users.get((index * 11) % users.size()),
                    constellations.get(index % constellations.size())
            ));
        }
        return constellationLikeRepository.saveAll(constellationLikes);
    }
}
