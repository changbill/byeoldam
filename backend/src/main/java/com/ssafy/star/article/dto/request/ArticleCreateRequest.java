package com.ssafy.star.article.dto.request;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.image.ImageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public record ArticleCreateRequest (

    String title,
    String tag,
    String description,
    DisclosureType disclosureType,
    ImageType imageType
//    private image;
){}