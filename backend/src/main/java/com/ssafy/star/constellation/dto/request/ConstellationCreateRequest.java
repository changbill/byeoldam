package com.ssafy.star.constellation.dto.request;

import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record ConstellationCreateRequest (
    String name,
    SharedType shared,
//   NoSQL outline;
    String description
){
}