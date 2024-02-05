package com.ssafy.star.constellation.domain;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "constellation_user")
@ToString
@Getter
@NoArgsConstructor
public class ConstellationUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "constellation_id")
    @Setter
    private ConstellationEntity constellationEntity;

    //TODO : user Entity에 OneToMany 연결할 것

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private UserEntity userEntity;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "constellation_user_role")
    private ConstellationUserRole constellationUserRole;

    public ConstellationUserEntity(ConstellationEntity constellationEntity, UserEntity userEntity, ConstellationUserRole constellationUserRole) {
        this.constellationEntity = constellationEntity;
        this.userEntity = userEntity;
        this.constellationUserRole = constellationUserRole;
    }

    public static ConstellationUserEntity of(ConstellationEntity constellationEntity, UserEntity userEntity, ConstellationUserRole constellationUserRole) {
        ConstellationUserEntity constellationUserEntity = new ConstellationUserEntity(constellationEntity, userEntity, constellationUserRole);
        return constellationUserEntity;
    }

    public void updateConstellationEntity(ConstellationEntity constellationEntity) {
        this.constellationEntity = constellationEntity;
    }

    public void updateUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
