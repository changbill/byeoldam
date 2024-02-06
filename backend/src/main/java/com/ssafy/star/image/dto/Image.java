package com.ssafy.star.image.dto;

import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.domain.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record Image(
        Long id,
        String name,
        String url,
        String thumbnailUrl,
        ImageType imageType
) {
    public static Image fromEntity(ImageEntity imageEntity){
        return new Image(
                imageEntity.getId(),
                imageEntity.getName(),
                imageEntity.getUrl(),
                imageEntity.getThumbnailUrl(),
                imageEntity.getImageType()
        );
    }

    public ImageEntity toEntity(){
        return ImageEntity.of(
                this.name,
                this.url,
                this.thumbnailUrl,
                this.imageType
        );
    }

}
