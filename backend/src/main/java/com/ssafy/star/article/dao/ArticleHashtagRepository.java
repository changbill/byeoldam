package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleHashtagEntity;

import java.util.Optional;

public interface ArticleHashtagRepository {

    ArticleHashtagEntity save(ArticleHashtagEntity articleHashtagEntity);

    Optional<ArticleHashtagEntity> findByTagName(String tagName);
}
