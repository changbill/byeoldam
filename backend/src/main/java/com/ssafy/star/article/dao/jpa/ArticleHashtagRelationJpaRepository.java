package com.ssafy.star.article.dao.jpa;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
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

    @Query("SELECT a FROM ArticleHashtagRelationEntity a WHERE a.articleHashtagEntity.tagName = :tagName")
    List<ArticleHashtagRelationEntity> findAllByTagName(@Param("tagName") String tagName);
}
