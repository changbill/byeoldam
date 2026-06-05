package com.ssafy.star.article.domain;

import com.ssafy.star.comment.domain.CommentEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE `article` SET deleted_at = NOW() where id=?")
public class ArticleEntity {

    // TODO : Article 인덱싱 ownerEntity 기준으로
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 105)
    private String title;

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.ALL)
    private Set<ArticleHashtagRelationEntity> articleHashtagRelationEntities = new HashSet<>();

    @Column(name = "hits", nullable = false)
    private long hits;

    @Column(name = "description", length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "disclosure", nullable = false)
    private DisclosureType disclosure;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "constellation_id")
    private ConstellationEntity constellationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity ownerEntity;

    @OneToMany(mappedBy = "articleEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private ImageEntity imageEntity;

    // TODO : modifiedAt 생성시 초기화되는 것 해결
    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    // 삭제 취소
    public void undoDeletion(){
        this.deletedAt = null;
    }

    public void addHits() { this.hits++; }

    public void selectConstellation(ConstellationEntity constellationEntity) { this.constellationEntity = constellationEntity; }

    public void update(String title, String description, DisclosureType disclosure) {
        this.title = title;
        this.description = description;
        this.disclosure = disclosure;
    }

    private ArticleEntity(
            String title,
            String description,
            DisclosureType disclosure,
            UserEntity ownerEntity,
            ConstellationEntity constellationEntity,
            ImageEntity imageEntity
    ) {
        this.title = title;
        this.description = description;
        this.disclosure = disclosure;
        this.ownerEntity = ownerEntity;
        this.constellationEntity = constellationEntity;
        this.imageEntity = imageEntity;
    }

    public static ArticleEntity of(
            String title,
            String description,
            DisclosureType disclosure,
            UserEntity ownerEntity,
            ConstellationEntity constellationEntity,
            ImageEntity imageEntity
    ){
        return new ArticleEntity(
                title,
                description,
                disclosure,
                ownerEntity,
                constellationEntity,
                imageEntity
        );
    }

}
