package com.ssafy.star.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRequest(
        @Schema(description = "사용자 닉네임", nullable = false, example = "abc_def")
        String nickname
) {
}