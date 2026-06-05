package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<ArticleEntity, Long> {

    boolean existsByIdAndDeletedAtIsNullAndDisclosure(
            Long articleId,
            DisclosureType disclosure
    );

    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.ownerEntity
            JOIN FETCH a.imageEntity
            LEFT JOIN FETCH a.constellationEntity
            WHERE a.ownerEntity = :ownerEntity
              AND a.deletedAt IS NULL
              AND a.disclosure = :disclosure
            """)
    List<ArticleEntity> findVisibleArticlesByOwner(
            @Param("ownerEntity") UserEntity ownerEntity,
            @Param("disclosure") DisclosureType disclosure
    );

    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.ownerEntity
            JOIN FETCH a.imageEntity
            LEFT JOIN FETCH a.constellationEntity
            WHERE a.ownerEntity = :ownerEntity
              AND a.deletedAt IS NULL
            """)
    List<ArticleEntity> findNotDeletedArticlesByOwner(@Param("ownerEntity") UserEntity ownerEntity);

    @EntityGraph(attributePaths = {"ownerEntity", "imageEntity", "constellationEntity"})
    Page<ArticleEntity> findAllByOwnerEntityAndDeletedAtIsNotNull(UserEntity ownerEntity, Pageable pageable);

    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.ownerEntity
            JOIN FETCH a.imageEntity
            JOIN FETCH a.constellationEntity
            WHERE a.constellationEntity = :constellationEntity
              AND a.deletedAt IS NULL
              AND (a.disclosure = :disclosure OR a.ownerEntity = :ownerEntity)
            """)
    List<ArticleEntity> findReadableArticlesInConstellation(
            @Param("constellationEntity") ConstellationEntity constellationEntity,
            @Param("disclosure") DisclosureType disclosure,
            @Param("ownerEntity") UserEntity ownerEntity
    );

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

    List<ArticleEntity> findByConstellationEntity(ConstellationEntity constellationEntity);

    @Query("""
            SELECT a
            FROM ArticleEntity a
            JOIN FETCH a.ownerEntity
            JOIN FETCH a.imageEntity
            WHERE a.constellationEntity IS NULL
                AND a.ownerEntity = :userEntity
                AND a.deletedAt IS NULL
            """)
    List<ArticleEntity> findUnassignedArticlesByOwner(@Param("userEntity") UserEntity userEntity);

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
}
