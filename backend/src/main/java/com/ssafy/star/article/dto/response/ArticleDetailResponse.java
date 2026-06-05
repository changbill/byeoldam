package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.dto.ArticleDetail;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.image.dto.response.ImageResponse;

import java.time.LocalDateTime;
import java.util.Set;

public record ArticleDetailResponse(
        Long id,
        String title,
        long hits,
        String description,
        DisclosureType disclosure,
        Set<String> articleHashtags,
        Long constellationId,
        String ownerEntityNickname,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        LocalDateTime deletedAt,
        ImageResponse imageResponse
) {
    public static ArticleDetailResponse fromArticleDetail(ArticleDetail articleDetail) {
        Long constellationId = null;
        if(articleDetail.constellation() != null) {
            constellationId = articleDetail.constellation().id();
        }

        return new ArticleDetailResponse(
                articleDetail.id(),
                articleDetail.title(),
                articleDetail.hits(),
                articleDetail.description(),
                articleDetail.disclosure(),
                articleDetail.articleHashtags(),
                constellationId,
                articleDetail.user().nickname(),
                articleDetail.createdAt(),
                articleDetail.modifiedAt(),
                articleDetail.deletedAt(),
                ImageResponse.fromImage(articleDetail.image())
        );
    }
}
