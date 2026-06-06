package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ConstellationRepository {

    ConstellationEntity save(ConstellationEntity constellationEntity);

    ConstellationEntity saveAndFlush(ConstellationEntity constellationEntity);

    void delete(ConstellationEntity constellationEntity);

    Optional<ConstellationEntity> findById(Long id);

    Page<ConstellationEntity> findAllByUserEntity(UserEntity userEntity, Pageable pageable);
}
