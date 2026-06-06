package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ConstellationLikeRepository {

    ConstellationLikeEntity save(ConstellationLikeEntity constellationLikeEntity);

    List<ConstellationLikeEntity> saveAll(List<ConstellationLikeEntity> constellationLikeEntities);

    void delete(ConstellationLikeEntity constellationLikeEntity);

    Optional<ConstellationLikeEntity> findByUserEntityAndConstellationEntity(
            UserEntity userEntity,
            ConstellationEntity constellationEntity
    );

    Integer countByConstellationEntity(ConstellationEntity constellationEntity);

    void deleteAllByConstellationEntity(ConstellationEntity constellationEntity);

    void deleteAllByUserEntity(UserEntity userEntity);

    Page<ConstellationLikeEntity> findAllByConstellationEntity(ConstellationEntity constellationEntity, Pageable pageable);

    Page<ConstellationLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable);
}
