package com.ssafy.star.article.domain;

import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "articleLike",
        indexes = {
                @Index(name = "idx_article_like_article_deleted", columnList = "article_id, deleted_at"),
                @Index(name = "idx_article_like_user_article_deleted", columnList = "user_id, article_id, deleted_at")
        }
)
@Getter
@Setter
@SQLDelete(sql = "UPDATE `article_like` SET deleted_at = NOW() where id=?")
public class ArticleLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "articleId", nullable = false)
    private ArticleEntity articleEntity;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 삭제 취소
    public void undoDeletion(){
        this.deletedAt = null;
    }

    @PrePersist
    void createdAt() { this.createdAt = LocalDateTime.from(LocalDateTime.now()); }

    protected ArticleLikeEntity() {}

    private ArticleLikeEntity(UserEntity userEntity, ArticleEntity articleEntity) {
        this.userEntity = userEntity;
        this.articleEntity = articleEntity;
    }

    public static ArticleLikeEntity of(UserEntity userEntity, ArticleEntity articleEntity) {
        return new ArticleLikeEntity(userEntity, articleEntity);
    }
}
