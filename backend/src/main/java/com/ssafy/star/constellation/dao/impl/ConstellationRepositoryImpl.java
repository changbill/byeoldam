package com.ssafy.star.constellation.dao.impl;

import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.dao.jpa.ConstellationJpaRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConstellationRepositoryImpl implements ConstellationRepository {

    private final ConstellationJpaRepository constellationJpaRepository;

    @Override
    public ConstellationEntity save(ConstellationEntity constellationEntity) {
        return constellationJpaRepository.save(constellationEntity);
    }

    @Override
    public ConstellationEntity saveAndFlush(ConstellationEntity constellationEntity) {
        return constellationJpaRepository.saveAndFlush(constellationEntity);
    }

    @Override
    public void delete(ConstellationEntity constellationEntity) {
        constellationJpaRepository.delete(constellationEntity);
    }

    @Override
    public Optional<ConstellationEntity> findById(Long id) {
        return constellationJpaRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return constellationJpaRepository.existsById(id);
    }

    @Override
    public Page<ConstellationEntity> findAllByUserEntity(UserEntity userEntity, Pageable pageable) {
        return constellationJpaRepository.findAllByUserEntity(userEntity, pageable);
    }
}
