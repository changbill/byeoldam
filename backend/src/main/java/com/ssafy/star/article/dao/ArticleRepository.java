package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
        // 게시물 상세 조회(지워지지 않은)
        @Query("SELECT COUNT(a) > 0 FROM ArticleEntity a WHERE a.id = :articleId AND a.deletedAt IS NULL AND (a.ownerEntity = :ownerEntity OR a.disclosure = 'VISIBLE')")
        boolean findByArticleIdAndNotDeleted(@Param("articleId") Long articleId, @Param("ownerEntity") UserEntity ownerEntity);

        /**
         * 유저 게시물 전체 조회(지워지지 않은)
          */
        // userEntity, deletedAt == Null, VISIBLE
        @Query("SELECT a FROM ArticleEntity a JOIN FETCH a.ownerEntity WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NULL AND a.disclosure = 'VISIBLE'")
        List<ArticleEntity> findAllByOwnerEntityAndNotDeletedAndDisclosure(@Param("ownerEntity") UserEntity ownerEntity);

        // userEntity, deletedAt == Null
        @Query("SELECT a FROM ArticleEntity a JOIN FETCH a.ownerEntity WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NULL")
        List<ArticleEntity> findAllByOwnerEntityAndNotDeleted(@Param("ownerEntity") UserEntity ownerEntity);

        /**
         * 휴지통 조회
         */
        @Query("SELECT a FROM ArticleEntity a WHERE a.ownerEntity = :ownerEntity AND a.deletedAt IS NOT NULL")
        Page<ArticleEntity> findAllByOwnerEntityAndDeleted(@Param("ownerEntity") UserEntity ownerEntity, Pageable pageable);

        List<ArticleEntity> findAllByOwnerEntity(UserEntity userEntity);

        // 별자리 검색 시 클릭, 해당 별자리 게시물 전체 조회
        @Query("SELECT a FROM ArticleEntity a JOIN FETCH a.ownerEntity JOIN FETCH a.constellationEntity WHERE a.constellationEntity = :constellationEntity AND a.deletedAt IS NULL AND (a.disclosure = 'VISIBLE' OR a.ownerEntity = :userEntity)")
        List<ArticleEntity> findAllByConstellationEntitySearch(@Param("constellationEntity") ConstellationEntity constellationEntity, @Param("userEntity") UserEntity userEntity);

        // 별자리 삭제 시 별자리의 모든 게시물 가져오기
//        @Query("SELECT a FROM ArticleEntity a WHERE a.constellationEntity = :constellationEntity")
        List<ArticleEntity> findByConstellationEntity(ConstellationEntity constellationEntity);

        // 미분류 별자리 게시물 전체 조회
        @Query("SELECT a FROM ArticleEntity a WHERE a.constellationEntity IS NULL AND a.ownerEntity = :userEntity AND a.deletedAt IS NULL")
        List<ArticleEntity> findAllByConstellationEntityNullAndOwnerEntity(@Param("userEntity") UserEntity userEntity);

        // 게시물 숫자
        @Query(value = "SELECT COUNT(*) FROM ArticleEntity entity WHERE entity.ownerEntity = :ownerEntity AND entity.deletedAt IS NULL")
        Integer countArticlesByUser(@Param("ownerEntity") UserEntity ownerEntity);
}

