package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    ArticleEntity save(ArticleEntity articleEntity);

    ArticleEntity saveAndFlush(ArticleEntity articleEntity);

    List<ArticleEntity> saveAll(List<ArticleEntity> articleEntities);

    void delete(ArticleEntity articleEntity);

    Optional<ArticleEntity> findById(Long articleId);

    List<ArticleEntity> findAll();

    boolean isVisibleArticle(Long articleId);

    List<ArticleEntity> findVisibleArticlesByOwner(UserEntity ownerEntity);

    List<ArticleEntity> findNotDeletedArticlesByOwner(UserEntity ownerEntity);

    Page<ArticleEntity> findDeletedArticlesByOwner(UserEntity ownerEntity, Pageable pageable);

    List<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity
    );

    Page<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity,
            Pageable pageable
    );

    List<ArticleEntity> findArticlesInConstellation(ConstellationEntity constellationEntity);

    List<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity);

    Page<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity, Pageable pageable);

    Integer countNotDeletedArticlesByOwner(UserEntity ownerEntity);
}
