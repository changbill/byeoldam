package com.ssafy.star.article.dto.request;

import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.image.ImageType;

import java.util.Set;

public record ArticleCreateWithNoConstellationRequest(

    String title,
    String description,
    DisclosureType disclosureType,
    ImageType imageType,
    Set<String> articleHashtagSet
){}