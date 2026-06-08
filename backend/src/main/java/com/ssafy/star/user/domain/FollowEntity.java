package com.ssafy.star.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(
        name = "follow",
        indexes = {
                @Index(name = "idx_follow_from_status_to", columnList = "from_user_id, status, to_user_id"),
                @Index(name = "idx_follow_from_to_status", columnList = "from_user_id, to_user_id, status")
        }
)
public class FollowEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private UserEntity toUser;

    private LocalDateTime requestDate;

    @Setter
    private LocalDateTime acceptDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @PrePersist
    void requestDate() {
        this.requestDate = LocalDateTime.from(LocalDateTime.now());
    }

    protected FollowEntity(){}

    private FollowEntity(UserEntity fromUser, UserEntity toUser, LocalDateTime acceptDate, ApprovalStatus status) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.acceptDate = acceptDate;
        this.status = status;
    }
    public static FollowEntity of(UserEntity fromUser, UserEntity toUser, LocalDateTime acceptDate, ApprovalStatus status) {
        return new FollowEntity(fromUser, toUser, acceptDate, status);
    }

}
