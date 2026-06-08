package com.ssafy.star.constellation.dao.impl;

import com.ssafy.star.constellation.dao.ConstellationLikeRepository;
import com.ssafy.star.constellation.dao.jpa.ConstellationLikeJpaRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public boolean existsByUserIdAndConstellationId(Long userId, Long constellationId) {
        return constellationLikeJpaRepository.existsByUserIdAndConstellationId(userId, constellationId);
    }

    @Override
    public int deleteByUserIdAndConstellationId(Long userId, Long constellationId) {
        return constellationLikeJpaRepository.deleteByUserIdAndConstellationId(userId, constellationId);
    }

    @Override
    public Integer countByConstellationEntity(ConstellationEntity constellationEntity) {
        return constellationLikeJpaRepository.countByConstellationEntity(constellationEntity);
    }

    @Override
    public Integer countByConstellationId(Long constellationId) {
        return constellationLikeJpaRepository.countByConstellationId(constellationId);
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
    public Page<ConstellationLikeEntity> findAllByConstellationEntity(ConstellationEntity constellationEntity, Pageable pageable) {
        return constellationLikeJpaRepository.findAllByConstellationEntity(constellationEntity, pageable);
    }

    @Override
    public Page<ConstellationLikeEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity, Pageable pageable) {
        return constellationLikeJpaRepository.findAllByUserEntityOrderByCreatedAtDesc(userEntity, pageable);
    }
}
