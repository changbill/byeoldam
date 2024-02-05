package com.ssafy.star.constellation.domain;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "constellation")
@ToString
@Getter
@SQLDelete(sql = "UPDATE constellation SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ConstellationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "name")
    private String name;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "shared")
    private SharedType shared;

    // TODO : outline
//    @Column(name = "outline")
//    private NoSQL outline;

    @Column(name = "hits")
    private Long hits = 0L;

    @Setter
    @Column(name = "description")
    private String description;

    @ToString.Exclude
    @OneToMany(mappedBy = "constellationEntity", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ConstellationUserEntity> constellationUserEntities = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "constellationEntity")
    private List<ArticleEntity> articleEntities;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.from(LocalDateTime.now());
    }

    @PreUpdate
    void modifiedAt() {
        this.modifiedAt = LocalDateTime.from(LocalDateTime.now());
    }

    public void increaseHits() {
        this.hits++;
    }

    // TODO : 별자리 Admin, user 나누는 로직 확인좀

    public void addUser(ConstellationUserEntity constellationUserEntity) {
        this.constellationUserEntities.add(constellationUserEntity);
        constellationUserEntity.updateConstellationEntity(this);
    }

    public void deleteUser(ConstellationUserEntity constellationUserEntity) {
        this.constellationUserEntities.remove(constellationUserEntity);
        constellationUserEntity.updateConstellationEntity(this);
    }

    public UserEntity getAdminEntity() {
        for (int i = 0; i < constellationUserEntities.size(); i++) {
            if(constellationUserEntities.get(i).getConstellationUserRole().equals(ConstellationUserRole.ADMIN)) {
                return constellationUserEntities.get(i).getUserEntity();
            }
        }
        return null;
    }

    protected ConstellationEntity() {
    }

    private ConstellationEntity(String name, SharedType shared, String description) {
        this.name = name;
        this.shared = shared;
        this.description = description;
    }

    public static ConstellationEntity of(String name, SharedType shared, String description) {
        ConstellationEntity constellationEntity = new ConstellationEntity(name, shared, description);
        return constellationEntity;
    }
}
