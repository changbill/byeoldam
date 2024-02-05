package com.ssafy.star.constellation.dto;

import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.domain.UserEntity;

public record ConstellationUser (
        Long id,
        Long constellationId,
        Long userId,
        ConstellationUserRole constellationUserRole
){

    public static ConstellationUser of(
            Long id,
            Long constellationId,
            Long userId,
            ConstellationUserRole constellationUserRole
    ) {
        return new ConstellationUser(
                id,
                constellationId,
                userId,
                constellationUserRole
        );
    }
    public static ConstellationUser fromEntity(ConstellationUserEntity entity){
        return new ConstellationUser(
                entity.getId(),
                entity.getConstellationEntity().getId(),
                entity.getUserEntity().getId(),
                entity.getConstellationUserRole()
        );
    }

    public static UserEntity getUserEntity(ConstellationUserEntity constellationUserEntity) {
        return constellationUserEntity.getUserEntity();
    }

    public static ConstellationEntity getConstellationEntity(ConstellationUserEntity constellationUserEntity) {
        return constellationUserEntity.getConstellationEntity();
    }
}
