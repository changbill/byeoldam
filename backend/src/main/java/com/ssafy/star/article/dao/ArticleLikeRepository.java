package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLikeEntity, Long> {
    @Query("SELECT entity FROM ArticleLikeEntity entity WHERE entity.articleEntity = :articleEntity AND entity.userEntity = :userEntity AND entity.deletedAt IS NULL")
    Optional<ArticleLikeEntity> findByUserEntityAndArticleEntity(UserEntity userEntity, ArticleEntity articleEntity);

    @Query(value = "SELECT COUNT(*) FROM ArticleLikeEntity entity WHERE entity.articleEntity =:articleEntity AND entity.deletedAt IS NULL")
    Integer countByArticleEntity(ArticleEntity articleEntity);

    @Modifying
    @Query("DELETE FROM ArticleLikeEntity e WHERE e.articleEntity = :articleEntity AND e.deletedAt IS NULL")
    void deleteAllByArticleEntity(ArticleEntity articleEntity);

    @Modifying
    @Query("DELETE FROM ArticleLikeEntity e WHERE e.userEntity = :userEntity AND e.deletedAt IS NULL")
    void deleteAllByUserEntity(UserEntity userEntity);

    @Query("SELECT entity FROM ArticleLikeEntity entity JOIN FETCH entity.articleEntity WHERE entity.articleEntity = :articleEntity AND entity.deletedAt IS NULL")
    List<ArticleLikeEntity> findAllByArticleEntity(@Param("articleEntity") ArticleEntity articleEntity);

    @Query("SELECT e FROM ArticleLikeEntity e JOIN FETCH e.userEntity WHERE e.userEntity = :userEntity AND e.deletedAt IS NULL ORDER BY e.createdAt DESC")
    Page<ArticleLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(@Param("userEntity") UserEntity userEntity, Pageable pageable);
}
