package com.ssafy.star.constellation.dao.jpa;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConstellationUserJpaRepository extends JpaRepository<ConstellationUserEntity, Long> {

    @Query("SELECT a FROM ConstellationUserEntity a WHERE a.userEntity = :userEntity AND a.constellationEntity = :constellationEntity")
    Optional<ConstellationUserEntity> findByUserEntityAndConstellationEntity(
            @Param("userEntity") UserEntity userEntity,
            @Param("constellationEntity") ConstellationEntity constellationEntity
    );

    @Query("SELECT cu FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity")
    List<ConstellationUserEntity> findByUserEntity(@Param("userEntity") UserEntity userEntity);


    @Query("""
        SELECT cu
        FROM ConstellationUserEntity  cu
        JOIN FETCH cu.userEntity u
        LEFT JOIN FETCH u.imageEntity
        WHERE cu.constellationEntity = :constellationEntity
""")
    List<ConstellationUserEntity> findByConstellationEntity(
            @Param("constellationEntity") ConstellationEntity constellationEntity
    );

    @Query(
            value = """
                    SELECT cu
                    FROM ConstellationUserEntity cu
                    JOIN FETCH cu.userEntity u
                    LEFT JOIN FETCH u.imageEntity
                    WHERE cu.constellationEntity = :constellationEntity
                    ORDER BY cu.id ASC
                    """,
            countQuery = """
                    SELECT COUNT(cu)
                    FROM ConstellationUserEntity cu
                    WHERE cu.constellationEntity = :constellationEntity
                    """
    )
    Page<ConstellationUserEntity> findByConstellationEntity(
            @Param("constellationEntity") ConstellationEntity constellationEntity,
            Pageable pageable
    );

    @Query("""
            SELECT cu
            FROM ConstellationUserEntity cu
            JOIN FETCH cu.constellationEntity
            JOIN FETCH cu.userEntity
            WHERE cu.constellationEntity IN :constellationEntities
            ORDER BY cu.constellationEntity.id ASC, cu.id ASC
            """)
    List<ConstellationUserEntity> findConstellationUserEntitiesByConstellationEntityIn(
            @Param("constellationEntities") Collection<ConstellationEntity> constellationEntities
    );

    @Query(
            value = """
                    SELECT c
                    FROM ConstellationUserEntity cu
                    JOIN ConstellationEntity c ON c.id = cu.constellationEntity.id
                    WHERE cu.userEntity = :userEntity
                    ORDER BY c.createdAt DESC
                    """,
            countQuery = "SELECT COUNT(cu) FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity"
    )
    Page<ConstellationEntity> findConstellationByUserEntity(@Param("userEntity") UserEntity userEntity, Pageable pageable);

    @Query(value = "SELECT COUNT(*) FROM ConstellationUserEntity entity WHERE entity.userEntity = :userEntity")
    Integer countConstellationByUser(@Param("userEntity") UserEntity userEntity);

    List<ConstellationUserEntity> findByUserEntityAndConstellationUserRole(
            UserEntity userEntity,
            ConstellationUserRole constellationUserRole
    );
}
