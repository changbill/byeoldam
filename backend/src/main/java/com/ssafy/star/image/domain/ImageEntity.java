package com.ssafy.star.image.domain;


import com.ssafy.star.image.ImageType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "image")
@Getter
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_name", nullable = false, length = 255)
    private String name;

    @Column(name = "image_url", nullable = false, length = 500)
    private String url;

    @Column(name = "thumbnail_url", nullable = false, length = 500)
    private String thumbnailUrl;

    @Column(name = "image_type", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    protected ImageEntity() {}

    private ImageEntity(String name, String url, String thumbnailUrl, ImageType imageType){
        this.name = name;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.imageType = imageType;
    }

    public static ImageEntity of(String name, String url, String thumbnailUrl, ImageType imageType){
        return new ImageEntity(name, url, thumbnailUrl, imageType);
    }

}
