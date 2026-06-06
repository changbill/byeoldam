package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleHashtagRelationRepository {

    ArticleHashtagRelationEntity save(ArticleHashtagRelationEntity articleHashtagRelationEntity);

    List<ArticleHashtagRelationEntity> findAllByArticleEntity(ArticleEntity articleEntity);

    void deleteByArticleEntity(ArticleEntity articleEntity);

    Page<ArticleEntity> findArticlesByTagName(String tagName, Pageable pageable);
}
