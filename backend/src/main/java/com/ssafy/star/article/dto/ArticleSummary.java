package com.ssafy.star.article.dto;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.image.dto.Image;

import java.time.LocalDateTime;

public record ArticleSummary(
        Long id,
        String title,
        long hits,
        String description,
        DisclosureType disclosure,
        Long constellationId,
        String ownerEntityNickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        Image image
) {
    public static ArticleSummary fromEntity(ArticleEntity articleEntity) {
        Long constellationId = null;
        if(articleEntity.getConstellationEntity() != null) {
            constellationId = articleEntity.getConstellationEntity().getId();
        }

        return new ArticleSummary(
                articleEntity.getId(),
                articleEntity.getTitle(),
                articleEntity.getHits(),
                articleEntity.getDescription(),
                articleEntity.getDisclosure(),
                constellationId,
                articleEntity.getOwnerEntity().getNickname(),
                articleEntity.getCreatedAt(),
                articleEntity.getModifiedAt(),
                articleEntity.getDeletedAt(),
                Image.fromEntity(articleEntity.getImageEntity())
        );
    }
}
