package com.ssafy.star.image.domain;

import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.dto.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "\"image\"")
@Getter
@ToString
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    @Column(nullable = false, length = 255)
    private String name;

    @Setter
    @NotNull
    @Column(nullable = false, length = 512)
    private String url;

    @Setter
    @Column(length = 512)
    private String thumbnailUrl;

    @Setter
    @NotNull
    @Column(nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    protected ImageEntity() {
    }

    private ImageEntity(String name, String url, String thumbnailUrl, ImageType imageType) {
        this.name = name;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.imageType = imageType;
    }

    public static ImageEntity of(String name, String url, String thumbnailUrl, ImageType imageType) {
        return new ImageEntity(name, url, thumbnailUrl, imageType);
    }

    public static ImageEntity of(String name, String url, ImageType imageType) {
        return of(name, url, null, imageType);
    }

    public static ImageEntity fromDto(Image dto) {
        return of(
                dto.name(),
                dto.url(),
                dto.thumbnailUrl(),
                dto.imageType()
        );
    }

}
