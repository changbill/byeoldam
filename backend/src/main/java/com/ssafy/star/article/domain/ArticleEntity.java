package com.ssafy.star.article.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.comment.domain.CommentEntity;

import com.ssafy.star.constellation.domain.ConstellationEntity;

import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ManyToAny;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
@SQLDelete(sql = "UPDATE `article` SET deleted_at = NOW() where id=?")
//@Where(clause = "deleted_at is NULL")
public class ArticleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 105)
    private String title;

    @Column(name = "tag", length = 255)
    private String tag;

    // TODO : Image

    @Column(name = "hits", nullable = false)
    private long hits;

    @Column(name = "description", length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "disclosure", nullable = false)
    private DisclosureType disclosure;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    private ConstellationEntity constellationEntity;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity ownerEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "articleEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CommentEntity> commentEntities;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    //Image(사진) Table의 FK
    //1:1관계
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Image")
//    private ImageEntity imageEntity;

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

    protected ArticleEntity() {}

    private ArticleEntity(
            String title,
            String tag,
            String description,
            DisclosureType disclosure,
            UserEntity ownerEntity,
            ConstellationEntity constellationEntity
    ) {
        this.title = title;
        this.tag = tag;
        this.description = description;
        this.disclosure = disclosure;
        this.ownerEntity = ownerEntity;
        this.constellationEntity = constellationEntity;
    }

    public static ArticleEntity of(String title, String tag, String description, DisclosureType disclosure, UserEntity ownerEntity, ConstellationEntity constellationEntity){
        ArticleEntity entity = new ArticleEntity(title, tag, description, disclosure,ownerEntity,constellationEntity);
        return entity;
    }

}
