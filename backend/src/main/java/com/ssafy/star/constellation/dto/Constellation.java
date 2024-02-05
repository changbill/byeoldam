package com.ssafy.star.constellation.dto;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public record Constellation (

    Long id,
    String name,
    SharedType shared,
//    NoSQL outline,
    long hits,
    String description,
    List<ConstellationUserEntity> constellationUserEntities,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt
)
{
    public static Constellation of(
            Long id,
            String name,
            SharedType shared,
            long hits,
            String description,
            List<ConstellationUserEntity> constellationUserEntities,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            LocalDateTime deletedAt
    ) {
        return new Constellation(
                id,
                name,
                shared,
                hits,
                description,
                constellationUserEntities,
                createdAt,
                modifiedAt,
                deletedAt
        );
    }

    public static Constellation fromEntity(ConstellationEntity entity){
        return new Constellation(
                entity.getId(),
                entity.getName(),
                entity.getShared(),
                // entity.getOutline(),
                entity.getHits(),
                entity.getDescription(),
                entity.getConstellationUserEntities(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }

    public ConstellationEntity toEntity(){
        return ConstellationEntity.of(
                name,
                shared,
                description
        );
    }
}

