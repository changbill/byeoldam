package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConstellationUserRepository {

    ConstellationUserEntity save(ConstellationUserEntity constellationUserEntity);

    ConstellationUserEntity saveAndFlush(ConstellationUserEntity constellationUserEntity);

    Optional<ConstellationUserEntity> findByUserEntityAndConstellationEntity(
            UserEntity userEntity,
            ConstellationEntity constellationEntity
    );

    List<ConstellationUserEntity> findByUserEntity(UserEntity userEntity);

    Page<ConstellationUserEntity> findByConstellationEntity(
            ConstellationEntity constellationEntity,
            Pageable pageable
    );

    List<ConstellationUserEntity> findConstellationUserEntitiesByConstellationEntityIn(
            Collection<ConstellationEntity> constellationEntities
    );

    List<ConstellationUserEntity> findAdminUsersByConstellationEntityIn(
            Collection<ConstellationEntity> constellationEntities
    );

    Page<ConstellationEntity> findConstellationByUserEntity(UserEntity userEntity, Pageable pageable);

    Integer countConstellationByUser(UserEntity userEntity);

    List<ConstellationUserEntity> findByUserEntityAndConstellationUserRole(
            UserEntity userEntity,
            ConstellationUserRole constellationUserRole
    );
}
