package com.ssafy.star.user.dto.response;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.user.dto.User;

import java.time.LocalDate;

public record UserResponse(
        //TODO: 사진, 팔로워, 팔로잉 추가
        String imageUrl,
        String email,
        String name,
        String nickname,
        String memo,
        DisclosureType disclosureType,
        LocalDate birthday

) {

    public static UserResponse fromUser(User dto) {
        return new UserResponse("imageUrl", dto.email(), dto.name(), dto.nickname(), dto.memo(), dto.disclosureType(), dto.birthday());
    }
}
