package com.ssafy.star.image.dto.response;

import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.image.dto.Image;

public record ImageResponse (
        String imageName,
        String imageUrl,
        String thumbnailUrl,
        ImageType imageType
){
    public static ImageResponse fromImage(Image image) {
        return new ImageResponse(
                image.name(),
                image.url(),
                image.thumbnailUrl(),
                image.imageType()
        );
    }

    public static ImageResponse fromEntity(ImageEntity imageEntity) {
        if(imageEntity == null) {
            return null;
        }
        return new ImageResponse(
                imageEntity.getName(),
                imageEntity.getUrl(),
                imageEntity.getThumbnailUrl(),
                imageEntity.getImageType()
        );
    }
}
