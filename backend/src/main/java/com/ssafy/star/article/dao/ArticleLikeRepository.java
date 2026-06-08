package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeRepository {

    ArticleLikeEntity save(ArticleLikeEntity articleLikeEntity);

    List<ArticleLikeEntity> saveAll(List<ArticleLikeEntity> articleLikeEntities);

    void delete(ArticleLikeEntity articleLikeEntity);

    Optional<ArticleLikeEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity);

    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    int deleteByUserIdAndArticleId(Long userId, Long articleId);

    Integer countByArticleEntity(ArticleEntity articleEntity);

    Integer countByArticleId(Long articleId);

    void deleteAllByArticleEntity(ArticleEntity articleEntity);

    void deleteAllByUserEntity(UserEntity userEntity);

    List<ArticleLikeEntity> findAllByArticleEntity(ArticleEntity articleEntity);

    Page<ArticleLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);
}
