package com.ssafy.star.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "comment", indexes = {
        @Index(name = "article_id", columnList = "article_id")
})
@Getter
@Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity articleEntity;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(updatable = false)
    private Long parentId;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    private Set<CommentEntity> childrenComments = new LinkedHashSet<>();

    @PrePersist
    void createdAt() { this.createdAt = LocalDateTime.from(LocalDateTime.now()); }

    @PreUpdate
    void modifiedAt() { this.modifiedAt = LocalDateTime.from(LocalDateTime.now()); }

    protected CommentEntity() {}

    private CommentEntity(UserEntity userEntity, ArticleEntity articleEntity, String content, Long parentId) {
        this.userEntity = userEntity;
        this.articleEntity = articleEntity;
        this.content = content;
        this.parentId = parentId;
    }

    public static CommentEntity of(UserEntity userEntity, ArticleEntity articleEntity, String content, Long parentId) {
        return new CommentEntity(userEntity, articleEntity, content, parentId);
    }
}

