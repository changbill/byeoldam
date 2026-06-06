package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.ApprovalStatus;
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

    default List<ArticleEntity> findNotDeletedArticlesByOwner(UserEntity ownerEntity) {
        return findArticlesByOwner(ownerEntity, false, Pageable.unpaged()).getContent();
    }

    Page<ArticleEntity> findArticlesByOwner(UserEntity ownerEntity, boolean visibleOnly, Pageable pageable);

    Page<ArticleEntity> findDeletedArticlesByOwner(UserEntity ownerEntity, Pageable pageable);

    default List<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity
    ) {
        return findReadableArticlesInConstellation(constellationEntity, userEntity, Pageable.unpaged()).getContent();
    }

    Page<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity,
            Pageable pageable
    );

    List<ArticleEntity> findArticlesInConstellation(ConstellationEntity constellationEntity);

    default List<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity) {
        return findUnassignedArticlesByOwner(ownerEntity, Pageable.unpaged()).getContent();
    }

    Page<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity, Pageable pageable);

    Integer countNotDeletedArticlesByOwner(UserEntity ownerEntity);

    Page<ArticleEntity> findFollowFeedLatestSort(UserEntity viewer, ApprovalStatus status, Pageable pageable);
}
