package com.ssafy.star.constellation.dao.jpa;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConstellationJpaRepository extends JpaRepository<ConstellationEntity, Long> {

    @Query(
            value = """
                    SELECT cu.constellationEntity
                    FROM ConstellationUserEntity cu
                    WHERE cu.userEntity = :userEntity
                    ORDER BY cu.constellationEntity.createdAt DESC
                    """,
            countQuery = "SELECT COUNT(cu) FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity"
    )
    Page<ConstellationEntity> findAllByUserEntity(@Param("userEntity") UserEntity userEntity, Pageable pageable);
}
