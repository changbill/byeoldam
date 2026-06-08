package com.ssafy.star.article.dao.impl;

import com.ssafy.star.article.dao.ArticleHashtagRepository;
import com.ssafy.star.article.dao.jpa.ArticleHashtagJpaRepository;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleHashtagRepositoryImpl implements ArticleHashtagRepository {

    private final ArticleHashtagJpaRepository articleHashtagJpaRepository;

    @Override
    public ArticleHashtagEntity save(ArticleHashtagEntity articleHashtagEntity) {
        return articleHashtagJpaRepository.save(articleHashtagEntity);
    }

    @Override
    public Optional<ArticleHashtagEntity> findByTagName(String tagName) {
        return articleHashtagJpaRepository.findByTagName(tagName);
    }
}
