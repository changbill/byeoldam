package com.ssafy.star.article.dao.impl;

import com.ssafy.star.article.dao.ArticleHashtagRelationRepository;
import com.ssafy.star.article.dao.jpa.ArticleHashtagRelationJpaRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleHashtagRelationRepositoryImpl implements ArticleHashtagRelationRepository {

    private final ArticleHashtagRelationJpaRepository articleHashtagRelationJpaRepository;

    @Override
    public ArticleHashtagRelationEntity save(ArticleHashtagRelationEntity articleHashtagRelationEntity) {
        return articleHashtagRelationJpaRepository.save(articleHashtagRelationEntity);
    }

    @Override
    public List<ArticleHashtagRelationEntity> findAllByArticleEntity(ArticleEntity articleEntity) {
        return articleHashtagRelationJpaRepository.findAllByArticleEntity(articleEntity);
    }

    @Override
    public void deleteByArticleEntity(ArticleEntity articleEntity) {
        articleHashtagRelationJpaRepository.deleteByArticleEntity(articleEntity);
    }

    @Override
    public List<ArticleHashtagRelationEntity> findAllByTagName(String tagName) {
        return articleHashtagRelationJpaRepository.findAllByTagName(tagName);
    }
}
