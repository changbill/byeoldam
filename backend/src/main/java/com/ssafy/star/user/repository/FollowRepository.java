package com.ssafy.star.user.repository;

import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByFromUserAndToUser(UserEntity from, UserEntity to);

    // 나의 팔로워 보기
    List<FollowEntity> findByToUserAndStatus(UserEntity toUser, ApprovalStatus status);
    // 나의 팔로잉 보기
    List<FollowEntity> findByFromUserAndStatus(UserEntity fromUser, ApprovalStatus status);

    // 나의 관계에 해당하는 것 모두 보기
    List<FollowEntity> findByFromUserOrToUser(UserEntity fromUser, UserEntity toUser);
    // 팔로우 관계 확인하기
    Optional<FollowEntity> findByFromUserAndToUserAndStatus(UserEntity fromUser, UserEntity toUser, ApprovalStatus status);

    //팔로잉 수 확인하기
    long countByFromUserAndStatus(UserEntity fromUser, ApprovalStatus status);

    //팔로워 수 확인하기
    long countByToUserAndStatus(UserEntity toUser, ApprovalStatus status);

    @Query("SELECT f.fromUser FROM FollowEntity f WHERE f.toUser = :toUser AND f.status = :status")
    List<UserEntity> findFromUsersByToUserAndStatus(@Param("toUser") UserEntity toUser, @Param("status")ApprovalStatus status);
}
