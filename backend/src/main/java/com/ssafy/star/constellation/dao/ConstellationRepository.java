package com.ssafy.star.constellation.dao;

import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConstellationRepository extends JpaRepository<ConstellationEntity, Long> {

    Optional<ConstellationEntity> findById(Long id);

    // 유저 별자리 전체 조회
    @Query("SELECT cu.constellationEntity FROM ConstellationUserEntity cu WHERE cu.userEntity = :userEntity")
    Page<ConstellationEntity> findAllByUserEntity(@Param("userEntity") UserEntity userEntity, Pageable pageable);


    //TODO : 윤곽선이 어떻게 나올지 몰라 NOSQL과 JOIN을 어떻게 해야 할지 몰라서 아직 적지 않았습니다.

    //TODO : 별자리와 게시글(별)을 조인해서 보여줘야합니다.
}

