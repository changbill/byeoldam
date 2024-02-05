package com.ssafy.star.article.dto;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


public record Article (

    Long id,
    String title,
    String tag,
//    private imageResponse image;
    long hits,
    String description,
    DisclosureType disclosure,
    ConstellationEntity constellationEntity,
    UserEntity ownerEntity,
    List<CommentEntity> commentEntities,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt

    ) {
    public static Article of(
        Long id,
        String title,
        String tag,
//    private imageResponse image;
        long hits,
        String description,
        DisclosureType disclosure,
        ConstellationEntity constellationEntity,
        UserEntity ownerEntity,
        List<CommentEntity> commentEntities,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt
) {
    return new Article(
            id,
            title,
            tag,
            hits,
            description,
            disclosure,
            constellationEntity,
            ownerEntity,
            commentEntities,
            createdAt,
            modifiedAt,
            deletedAt
    );
}

    public static Article fromEntity(ArticleEntity entity){
        return new Article(
                entity.getId(),
                entity.getTitle(),
                entity.getTag(),
                // entity.getOutline(),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                entity.getConstellationEntity(),
                entity.getOwnerEntity(),
                entity.getCommentEntities(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt()
        );
    }

    public ArticleEntity toEntity(){
        return ArticleEntity.of(
                title,
                tag,
                description,
                disclosure,
                ownerEntity,
                constellationEntity
        );
    }
}
