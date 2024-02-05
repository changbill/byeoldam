package com.ssafy.star.article.dto.response;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse (
    Long id,
    String title,
    String tag,
//    imageResponse image,
    DisclosureType disclosure,
    long hits,
    String description,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    LocalDateTime deletedAt
){

    public static ArticleResponse fromArticle(Article article) {
        return new ArticleResponse(
                article.id(),
                article.title(),
                article.tag(),
//                ImageResponse.fromImage(Article.getImage()),
                article.disclosure(),
                article.hits(),
                article.description(),
                article.createdAt(),
                article.modifiedAt(),
                article.deletedAt()
        );
    }
}