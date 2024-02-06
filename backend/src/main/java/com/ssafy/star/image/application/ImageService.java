package com.ssafy.star.image.application;

import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.dao.ImageRepository;
import com.ssafy.star.image.domain.ImageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImage(String name, String url, String thumbnailUrl, ImageType imageType){
        imageRepository.save(ImageEntity.of(name, url, thumbnailUrl, imageType));
    }

}
