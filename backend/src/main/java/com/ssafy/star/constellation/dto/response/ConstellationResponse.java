package com.ssafy.star.constellation.dto.response;

import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


public record ConstellationResponse (
        Long Id,
    String name,
    SharedType shared,
    // TODO : 외곽선
    long hits,
    String description,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt

) {

    public static ConstellationResponse fromConstellation(Constellation dto) {
        return new ConstellationResponse(
                dto.id(),
                dto.name(),
                dto.shared(),
                // const.getOutline(),
                dto.hits(),
                dto.description(),
                dto.createdAt(),
                dto.modifiedAt(),
                dto.deletedAt()
        );
    }
}