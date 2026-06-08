package com.ssafy.star.constellation.dao.impl;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.dao.jpa.ConstellationUserJpaRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConstellationUserRepositoryImpl implements ConstellationUserRepository {

    private final ConstellationUserJpaRepository constellationUserJpaRepository;

    @Override
    public ConstellationUserEntity save(ConstellationUserEntity constellationUserEntity) {
        return constellationUserJpaRepository.save(constellationUserEntity);
    }

    @Override
    public ConstellationUserEntity saveAndFlush(ConstellationUserEntity constellationUserEntity) {
        return constellationUserJpaRepository.saveAndFlush(constellationUserEntity);
    }

    @Override
    public Optional<ConstellationUserEntity> findByUserEntityAndConstellationEntity(
            UserEntity userEntity,
            ConstellationEntity constellationEntity
    ) {
        return constellationUserJpaRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity);
    }

    @Override
    public List<ConstellationUserEntity> findByUserEntity(UserEntity userEntity) {
        return constellationUserJpaRepository.findByUserEntity(userEntity);
    }

    @Override
    public Page<ConstellationUserEntity> findByConstellationEntity(
            ConstellationEntity constellationEntity,
            Pageable pageable
    ) {
        return constellationUserJpaRepository.findByConstellationEntity(constellationEntity, pageable);
    }

    @Override
    public List<ConstellationUserEntity> findConstellationUserEntitiesByConstellationEntityIn(
            Collection<ConstellationEntity> constellationEntities
    ) {
        if (constellationEntities.isEmpty()) {
            return List.of();
        }

        return constellationUserJpaRepository.findConstellationUserEntitiesByConstellationEntityIn(constellationEntities);
    }

    @Override
    public List<ConstellationUserEntity> findAdminUsersByConstellationEntityIn(
            Collection<ConstellationEntity> constellationEntities
    ) {
        if (constellationEntities.isEmpty()) {
            return List.of();
        }

        return constellationUserJpaRepository.findAdminUsersByConstellationEntityIn(
                constellationEntities,
                ConstellationUserRole.ADMIN
        );
    }

    @Override
    public Page<ConstellationEntity> findConstellationByUserEntity(UserEntity userEntity, Pageable pageable) {
        return constellationUserJpaRepository.findConstellationByUserEntity(userEntity, pageable);
    }

    @Override
    public Integer countConstellationByUser(UserEntity userEntity) {
        return constellationUserJpaRepository.countConstellationByUser(userEntity);
    }

    @Override
    public List<ConstellationUserEntity> findByUserEntityAndConstellationUserRole(
            UserEntity userEntity,
            ConstellationUserRole constellationUserRole
    ) {
        return constellationUserJpaRepository.findByUserEntityAndConstellationUserRole(
                userEntity,
                constellationUserRole
        );
    }
}
