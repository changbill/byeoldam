package com.ssafy.star.article.dto;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.image.dto.Image;
import com.ssafy.star.user.dto.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDetail(
        Long id,
        String title,
        long hits,
        String description,
        DisclosureType disclosure,
        Set<String> articleHashtags,
        Constellation constellation,
        User user,
        List<CommentDto> commentList,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        Image image
) {
    public static ArticleDetail fromEntity(ArticleEntity entity) {
        return new ArticleDetail(
                entity.getId(),
                entity.getTitle(),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                mapHashtags(entity),
                mapConstellation(entity.getConstellationEntity()),
                User.fromEntity(entity.getOwnerEntity()),
                mapComments(entity.getCommentEntities()),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt(),
                Image.fromEntity(entity.getImageEntity())
        );
    }

    private static Set<String> mapHashtags(ArticleEntity entity) {
        if(entity.getArticleHashtagRelationEntities() == null) return null;
        return entity.getArticleHashtagRelationEntities().stream()
                .map(ArticleHashtagRelationEntity::getArticleHashtagEntity)
                .filter(Objects::nonNull)
                .map(ArticleHashtagEntity::getTagName)
                .collect(Collectors.toSet());
    }

    private static Constellation mapConstellation(ConstellationEntity entity) {
        if (entity == null) return null;
        return Constellation.fromEntity(entity);
    }

    private static List<CommentDto> mapComments(List<CommentEntity> commentEntities) {
        return commentEntities.stream().map(CommentDto::from).toList();
    }
}
