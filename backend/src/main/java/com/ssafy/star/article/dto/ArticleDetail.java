package com.ssafy.star.article.dto;

import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.image.dto.Image;
import com.ssafy.star.user.dto.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
}
