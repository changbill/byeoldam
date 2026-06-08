package com.ssafy.star.article.dao.jpa;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleHashtagRelationJpaRepository extends JpaRepository<ArticleHashtagRelationEntity, Long> {

    List<ArticleHashtagRelationEntity> findAllByArticleEntity(ArticleEntity articleEntity);

    @Modifying
    @Query("DELETE FROM ArticleHashtagRelationEntity a WHERE a.articleEntity = :articleEntity")
    void deleteByArticleEntity(@Param("articleEntity") ArticleEntity articleEntity);

    @Query(
            value = """
                    SELECT a
                    FROM ArticleHashtagRelationEntity relation
                    JOIN relation.articleEntity a
                    JOIN FETCH a.ownerEntity
                    JOIN FETCH a.imageEntity
                    LEFT JOIN FETCH a.constellationEntity
                    WHERE relation.articleHashtagEntity.tagName = :tagName
                    ORDER BY a.createdAt DESC, a.id DESC
                    """,
            countQuery = """
                    SELECT COUNT(relation)
                    FROM ArticleHashtagRelationEntity relation
                    WHERE relation.articleHashtagEntity.tagName = :tagName
                    """
    )
    Page<ArticleEntity> findArticlesByTagName(@Param("tagName") String tagName, Pageable pageable);
}
