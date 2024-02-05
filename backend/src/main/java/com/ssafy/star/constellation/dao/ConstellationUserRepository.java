package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConstellationUserRepository extends JpaRepository<ConstellationUserEntity, Long> {
    @Query("SELECT a FROM ConstellationUserEntity a WHERE a.userEntity = :userEntity AND a.constellationEntity = :constellationEntity")
    Optional<ConstellationUserEntity> findByUserEntityAndConstellationEntity(@Param("userEntity") UserEntity userEntity,@Param("constellationEntity") ConstellationEntity constellationEntity);

    @Query("SELECT cu FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity")
    Page<ConstellationUserEntity> findConstellationUserEntitiesByUserEntity(@Param("userEntity") UserEntity userEntity, Pageable pageable);

    @Query("SELECT cu FROM ConstellationUserEntity cu WHERE cu.constellationEntity = :constellationEntity")
    Page<ConstellationUserEntity> findConstellationUserEntitiesByConstellationEntity(@Param("constellationEntity") ConstellationEntity constellationEntity, Pageable pageable);
}
