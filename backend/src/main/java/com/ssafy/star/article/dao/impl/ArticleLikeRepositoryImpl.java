package com.ssafy.star.article.dao.impl;

import com.ssafy.star.article.dao.ArticleLikeRepository;
import com.ssafy.star.article.dao.jpa.ArticleLikeJpaRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepositoryImpl implements ArticleLikeRepository {

    private final ArticleLikeJpaRepository articleLikeJpaRepository;

    @Override
    public ArticleLikeEntity save(ArticleLikeEntity articleLikeEntity) {
        return articleLikeJpaRepository.save(articleLikeEntity);
    }

    @Override
    public List<ArticleLikeEntity> saveAll(List<ArticleLikeEntity> articleLikeEntities) {
        return articleLikeJpaRepository.saveAll(articleLikeEntities);
    }

    @Override
    public void delete(ArticleLikeEntity articleLikeEntity) {
        articleLikeJpaRepository.delete(articleLikeEntity);
    }

    @Override
    public Optional<ArticleLikeEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity) {
        return articleLikeJpaRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity);
    }

    @Override
    public Integer countByArticleEntity(ArticleEntity articleEntity) {
        return articleLikeJpaRepository.countByArticleEntity(articleEntity);
    }

    @Override
    public void deleteAllByArticleEntity(ArticleEntity articleEntity) {
        articleLikeJpaRepository.deleteAllByArticleEntity(articleEntity);
    }

    @Override
    public void deleteAllByUserEntity(UserEntity userEntity) {
        articleLikeJpaRepository.deleteAllByUserEntity(userEntity);
    }

    @Override
    public List<ArticleLikeEntity> findAllByArticleEntity(ArticleEntity articleEntity) {
        return articleLikeJpaRepository.findAllByArticleEntity(articleEntity);
    }

    @Override
    public Page<ArticleLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable) {
        return articleLikeJpaRepository.findAllByUserEntityOrderByCreatedAtDesc(userEntity, pageable);
    }
}
