package com.ssafy.star.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.user.domain.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
public record User(
        Long id,
        String email,
        String password,
        String name,
        String nickname,
        String memo,
        DisclosureType disclosureType,
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthday,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime modifiedAt,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime deletedAt
) {
    public static User of(Long id, String email, String password, String name, String nickname, String memo,
                          DisclosureType disclosureType, LocalDate birthday, LocalDateTime createdAt,
                          LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        return new User(
                id,
                email,
                password,
                name,
                nickname,
                memo,
                disclosureType,
                birthday,
                createdAt,
                modifiedAt,
                deletedAt
        );
    }

    public static User fromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getDisclosureType(),
                entity.getBirthday(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }

    public UserEntity toEntity() {
        return UserEntity.of(
                email,
                name,
                password,
                nickname,
                memo,
                disclosureType,
                birthday
        );
    }

}
