package com.ssafy.star.article.dao.jpa;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {

    boolean existsByIdAndDeletedAtIsNullAndDisclosure(
            Long articleId,
            DisclosureType disclosure
    );

    @Query(
            value = """
                    SELECT a
                    FROM ArticleEntity a
                    JOIN FETCH a.ownerEntity
                    JOIN FETCH a.imageEntity
                    LEFT JOIN FETCH a.constellationEntity
                    WHERE a.ownerEntity = :ownerEntity
                      AND a.deletedAt IS NULL
                      AND (:visibleOnly = false OR a.disclosure = :visibleDisclosure)
                    ORDER BY a.createdAt DESC, a.id DESC
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM ArticleEntity a
                    WHERE a.ownerEntity = :ownerEntity
                      AND a.deletedAt IS NULL
                      AND (:visibleOnly = false OR a.disclosure = :visibleDisclosure)
                    """
    )
    Page<ArticleEntity> findArticlesByOwner(
            @Param("ownerEntity") UserEntity ownerEntity,
            @Param("visibleOnly") boolean visibleOnly,
            @Param("visibleDisclosure") DisclosureType visibleDisclosure,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"ownerEntity", "imageEntity", "constellationEntity"})
    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.ownerEntity
            JOIN FETCH a.imageEntity
            LEFT JOIN FETCH a.constellationEntity
            WHERE a.ownerEntity = :ownerEntity
                AND a.deletedAt IS NOT NULL
            """)
    Page<ArticleEntity> findAllByOwnerEntityAndDeletedAtIsNotNull(UserEntity ownerEntity, Pageable pageable);

    @Query(
            value = """
                    SELECT a
                    FROM ArticleEntity a
                    JOIN FETCH a.ownerEntity
                    JOIN FETCH a.imageEntity
                    JOIN FETCH a.constellationEntity
                    WHERE a.constellationEntity = :constellationEntity
                      AND a.deletedAt IS NULL
                      AND (a.disclosure = :disclosure OR a.ownerEntity = :ownerEntity)
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM ArticleEntity a
                    WHERE a.constellationEntity = :constellationEntity
                      AND a.deletedAt IS NULL
                      AND (a.disclosure = :disclosure OR a.ownerEntity = :ownerEntity)
                    """
    )
    Page<ArticleEntity> findReadableArticlesInConstellation(
            @Param("constellationEntity") ConstellationEntity constellationEntity,
            @Param("disclosure") DisclosureType disclosure,
            @Param("ownerEntity") UserEntity ownerEntity,
            Pageable pageable
    );

    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.imageEntity
            WHERE a.constellationEntity IN :constellationEntities
              AND a.deletedAt IS NULL
              AND (a.disclosure = :disclosure OR a.ownerEntity = :ownerEntity)
            ORDER BY a.constellationEntity.id ASC, a.createdAt DESC, a.id DESC
            """)
    List<ArticleEntity> findReadableArticlesInConstellations(
            @Param("constellationEntities") Collection<ConstellationEntity> constellationEntities,
            @Param("disclosure") DisclosureType disclosure,
            @Param("ownerEntity") UserEntity ownerEntity
    );

    List<ArticleEntity> findByConstellationEntity(ConstellationEntity constellationEntity);

    @Query(
            value = """
                    SELECT a
                    FROM ArticleEntity a
                    JOIN FETCH a.ownerEntity
                    JOIN FETCH a.imageEntity
                    WHERE a.constellationEntity IS NULL
                        AND a.ownerEntity = :userEntity
                        AND a.deletedAt IS NULL
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM ArticleEntity a
                    WHERE a.constellationEntity IS NULL
                        AND a.ownerEntity = :userEntity
                        AND a.deletedAt IS NULL
                    """
    )
    Page<ArticleEntity> findUnassignedArticlesByOwner(
            @Param("userEntity") UserEntity userEntity,
            Pageable pageable
    );

    Integer countByOwnerEntityAndDeletedAtIsNull(UserEntity ownerEntity);

    @Query(
            value = """
                    SELECT a
                    FROM ArticleEntity a
                    JOIN FETCH a.ownerEntity
                    JOIN FETCH a.imageEntity
                    LEFT JOIN FETCH a.constellationEntity
                    WHERE a.deletedAt IS NULL
                      AND a.ownerEntity.id IN (
                          SELECT f.toUser.id
                          FROM FollowEntity f
                          WHERE f.fromUser = :viewer
                            AND f.status = :status
                      )
                    ORDER BY a.createdAt DESC, a.id DESC
                    """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM ArticleEntity a
                    WHERE a.deletedAt IS NULL
                      AND a.ownerEntity.id IN (
                          SELECT f.toUser.id
                          FROM FollowEntity f
                          WHERE f.fromUser = :viewer
                            AND f.status = :status
                      )
                    """
    )
    Page<ArticleEntity> findFollowFeedLatestSort(UserEntity viewer, ApprovalStatus status, Pageable pageable);
}
