package com.ssafy.star.constellation.dao.impl;

import com.ssafy.star.constellation.dao.ConstellationLikeRepository;
import com.ssafy.star.constellation.dao.jpa.ConstellationLikeJpaRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConstellationLikeRepositoryImpl implements ConstellationLikeRepository {

    private final ConstellationLikeJpaRepository constellationLikeJpaRepository;

    @Override
    public ConstellationLikeEntity save(ConstellationLikeEntity constellationLikeEntity) {
        return constellationLikeJpaRepository.save(constellationLikeEntity);
    }

    @Override
    public List<ConstellationLikeEntity> saveAll(List<ConstellationLikeEntity> constellationLikeEntities) {
        return constellationLikeJpaRepository.saveAll(constellationLikeEntities);
    }

    @Override
    public void delete(ConstellationLikeEntity constellationLikeEntity) {
        constellationLikeJpaRepository.delete(constellationLikeEntity);
    }

    @Override
    public Optional<ConstellationLikeEntity> findByUserEntityAndConstellationEntity(
            UserEntity userEntity,
            ConstellationEntity constellationEntity
    ) {
        return constellationLikeJpaRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity);
    }

    @Override
    public Integer countByConstellationEntity(ConstellationEntity constellationEntity) {
        return constellationLikeJpaRepository.countByConstellationEntity(constellationEntity);
    }

    @Override
    public void deleteAllByConstellationEntity(ConstellationEntity constellationEntity) {
        constellationLikeJpaRepository.deleteAllByConstellationEntity(constellationEntity);
    }

    @Override
    public void deleteAllByUserEntity(UserEntity userEntity) {
        constellationLikeJpaRepository.deleteAllByUserEntity(userEntity);
    }

    @Override
    public List<ConstellationLikeEntity> findAllByConstellationEntity(ConstellationEntity constellationEntity, Sort sort) {
        return constellationLikeJpaRepository.findAllByConstellationEntity(constellationEntity, sort);
    }

    @Override
    public Page<ConstellationLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable) {
        return constellationLikeJpaRepository.findAllByUserEntityOrderByCreatedAtDesc(userEntity, pageable);
    }
}
