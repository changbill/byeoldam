package com.ssafy.star.support.performance;

import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.support.PerformanceProfileSupport;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "PERFORMANCE_BASELINE", matches = "true")
class BaselinePerformanceMeasurementTest extends PerformanceProfileSupport {

    private static final int WARM_UP = 2;
    private static final int ITERATIONS = 5;
    private static final String BASELINE_EMAIL = "perf-user-0@example.com";
    private static final String HEAVY_ARTICLE_OWNER_NICKNAME = "perfuser1";
    private static final String HEAVY_CONSTELLATION_OWNER_NICKNAME = "perfuser2";

    @Autowired
    ArticleService articleService;
    @Autowired
    ConstellationService constellationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ConstellationRepository constellationRepository;
    @Autowired
    PerformanceTimer timer;
    @Autowired
    EntityManager entityManager;

    @Test
    void measureLargeDatasetBaseline() {
        UserEntity baselineUser = requireUser(BASELINE_EMAIL);
        UserEntity heavyArticleOwner = requireUserByNickname(HEAVY_ARTICLE_OWNER_NICKNAME);
        UserEntity heavyConstellationOwner = requireUserByNickname(HEAVY_CONSTELLATION_OWNER_NICKNAME);
        ArticleEntity visibleArticle = requireVisibleArticle();
        ConstellationEntity constellationWithVisibleArticle = requireConstellationWithVisibleArticle();
        ConstellationEntity heavyOwnerConstellation = requireConstellationOwnedBy(heavyConstellationOwner);

        List<PerformanceMeasurement> measurements = new ArrayList<>();
        measurements.add(timer.measure(
                "article.followFeed(page=0,size=20)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(articleService.followFeed(BASELINE_EMAIL, PageRequest.of(0, 20)).getContent()).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "article.userArticleList(heavyOwner)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(articleService.userArticleList(heavyArticleOwner.getNickname(), BASELINE_EMAIL)).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "article.detail(visibleArticle)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(articleService.detail(visibleArticle.getId(), BASELINE_EMAIL).id()).isEqualTo(visibleArticle.getId())
        ));
        measurements.add(timer.measure(
                "article.articlesInConstellation",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(articleService.articlesInConstellation(constellationWithVisibleArticle.getId(), BASELINE_EMAIL)).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "article.articlesInNoConstellation(heavyOwner)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(articleService.articlesInNoConstellation(heavyArticleOwner.getEmail())).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "constellation.myConstellations(heavyOwner)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(constellationService.myConstellations(heavyConstellationOwner.getEmail())).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "constellation.userConstellations(heavyOwner)",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(constellationService.userConstellations(heavyConstellationOwner.getNickname(), BASELINE_EMAIL)).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "constellation.requestModifyConstellation",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(constellationService.requestModifyConstellation(heavyConstellationOwner.getEmail(), heavyOwnerConstellation.getId())).isNotNull()
        ));
        measurements.add(timer.measure(
                "constellation.findConstellationUsers",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(constellationService.findConstellationUsers(constellationWithVisibleArticle.getId())).isNotEmpty()
        ));
        measurements.add(timer.measure(
                "constellation.likeList",
                WARM_UP,
                ITERATIONS,
                () -> assertThat(constellationService.likeList(constellationWithVisibleArticle.getId())).isNotEmpty()
        ));

        System.out.println();
        System.out.println("baselineUser=" + baselineUser.getEmail()
                + " heavyArticleOwner=" + heavyArticleOwner.getNickname()
                + " heavyConstellationOwner=" + heavyConstellationOwner.getNickname()
                + " articleId=" + visibleArticle.getId()
                + " visibleArticleConstellationId=" + constellationWithVisibleArticle.getId()
                + " heavyOwnerConstellationId=" + heavyOwnerConstellation.getId());
        System.out.println("| API | dataset | avg ms | p50 ms | p95 ms | p99 ms | min ms | max ms | avg queries | query range |");
        System.out.println("| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | ---: |");
        measurements.forEach(measurement -> System.out.println(measurement.markdownRow()));
    }

    private UserEntity requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException(seedRequiredMessage("user: " + email)));
    }

    private UserEntity requireUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalStateException(seedRequiredMessage("user: " + nickname)));
    }

    private ArticleEntity requireVisibleArticle() {
        return articleRepository.findAll().stream()
                .filter(article -> article.getTitle().startsWith("PERF-ARTICLE-"))
                .filter(article -> article.getDeletedAt() == null)
                .filter(article -> "VISIBLE".equals(article.getDisclosure().name()))
                .min(Comparator.comparing(ArticleEntity::getId))
                .orElseThrow(() -> new IllegalStateException(seedRequiredMessage("visible performance article")));
    }

    private ConstellationEntity requireConstellationWithVisibleArticle() {
        return entityManager.createQuery("""
                        SELECT a.constellationEntity
                        FROM ArticleEntity a
                        WHERE a.title LIKE 'PERF-ARTICLE-%'
                          AND a.deletedAt IS NULL
                          AND a.disclosure = 'VISIBLE'
                          AND a.constellationEntity IS NOT NULL
                ORDER BY a.id
                """, ConstellationEntity.class)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(seedRequiredMessage("constellation with visible performance article")));
    }

    private ConstellationEntity requireConstellationOwnedBy(UserEntity owner) {
        return constellationRepository.findAllByUserEntity(owner).stream()
                .filter(constellation -> constellation.getName().startsWith("PERF-CONSTELLATION-"))
                .min(Comparator.comparing(ConstellationEntity::getId))
                .orElseThrow(() -> new IllegalStateException(seedRequiredMessage("performance constellation owned by " + owner.getNickname())));
    }

    private String seedRequiredMessage(String missingTarget) {
        return "Run LARGE seed once against the performance DB first. Missing " + missingTarget
                + ". Command: $env:PERFORMANCE_SEED='true'; .\\gradlew.bat test --tests "
                + "\"com.ssafy.star.support.performance.PerformanceDataSeedTest.recreateLargeDataset\"";
    }
}
