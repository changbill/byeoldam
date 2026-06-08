package com.ssafy.star.constellation.dao.jpa;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConstellationLikeJpaRepository extends JpaRepository<ConstellationLikeEntity, Long> {

    Optional<ConstellationLikeEntity> findByUserEntityAndConstellationEntity(
            UserEntity userEntity,
            ConstellationEntity constellationEntity
    );

    @Query("""
            SELECT COUNT(entity) > 0
            FROM ConstellationLikeEntity entity
            WHERE entity.userEntity.id = :userId
              AND entity.constellationEntity.id = :constellationId
            """)
    boolean existsByUserIdAndConstellationId(
            @Param("userId") Long userId,
            @Param("constellationId") Long constellationId
    );

    @Modifying
    @Query("""
            DELETE FROM ConstellationLikeEntity entity
            WHERE entity.userEntity.id = :userId
              AND entity.constellationEntity.id = :constellationId
            """)
    int deleteByUserIdAndConstellationId(
            @Param("userId") Long userId,
            @Param("constellationId") Long constellationId
    );

    @Query(value = "SELECT COUNT(*) FROM ConstellationLikeEntity entity WHERE entity.constellationEntity =:constellationEntity")
    Integer countByConstellationEntity(ConstellationEntity constellationEntity);

    @Query("""
            SELECT COUNT(entity)
            FROM ConstellationLikeEntity entity
            WHERE entity.constellationEntity.id = :constellationId
            """)
    Integer countByConstellationId(@Param("constellationId") Long constellationId);

    void deleteAllByConstellationEntity(ConstellationEntity constellationEntity);

    void deleteAllByUserEntity(UserEntity userEntity);

    @Query(value = """
            SELECT a
            FROM ConstellationLikeEntity a
            JOIN FETCH a.userEntity u
            LEFT JOIN FETCH u.imageEntity
            WHERE a.constellationEntity = :constellationEntity
            ORDER BY a.createdAt DESC
            """,
            countQuery = """
                    SELECT COUNT(a)
                    FROM ConstellationLikeEntity a
                    WHERE a.constellationEntity = :constellationEntity
                    """
    )
    Page<ConstellationLikeEntity> findAllByConstellationEntity(ConstellationEntity constellationEntity, Pageable pageable);

    Page<ConstellationLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);
}
