package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.dto.ArticleSummary;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.image.dto.response.ImageResponse;

import java.time.LocalDateTime;

public record ArticleSummaryResponse(
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
        ImageResponse imageResponse
) {
    public static ArticleSummaryResponse fromArticleSummary(ArticleSummary articleSummary) {
        return new ArticleSummaryResponse(
                articleSummary.id(),
                articleSummary.title(),
                articleSummary.hits(),
                articleSummary.description(),
                articleSummary.disclosure(),
                articleSummary.constellationId(),
                articleSummary.ownerEntityNickname(),
                articleSummary.createdAt(),
                articleSummary.modifiedAt(),
                articleSummary.deletedAt(),
                ImageResponse.fromImage(articleSummary.image())
        );
    }
}
