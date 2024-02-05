package com.ssafy.star.user.domain;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Table(name = "user_account")
@SQLDelete(sql = "UPDATE user_account SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {
    //TODO: 사진 프로필 추가 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false, length = 50)
    private String name;

    @Setter
    @Column(nullable = false, length = 32)
    private String nickname;

    @Setter
    @Column(length = 512)
    private String memo;

    @Setter
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DisclosureType disclosureType = DisclosureType.VISIBLE;

    @ToString.Exclude
    @OneToMany(mappedBy = "userEntity", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConstellationUserEntity> constellationUserEntities = new ArrayList<>();

    @Setter
    private LocalDate birthday;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    protected UserEntity() {
    }

    private UserEntity(String email, String password, String name, String nickname, String memo, DisclosureType disclosureType, LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.memo = memo;
        this.disclosureType = disclosureType;
        this.birthday = birthday;
    }

    public static UserEntity of(String email, String password, String name, String nickname) {
        return of(email, password, name, nickname, null, null, null);
    }

    public static UserEntity of(String email, String password, String name, String nickname, String memo, DisclosureType disclosureType, LocalDate birthday) {
        return new UserEntity(email, password, name, nickname, memo, disclosureType, birthday);
    }

}
