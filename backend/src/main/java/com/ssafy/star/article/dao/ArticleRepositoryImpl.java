package com.ssafy.star.article.dao;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleJpaRepository articleJpaRepository;

    @Override
    public ArticleEntity save(ArticleEntity articleEntity) {
        return articleJpaRepository.save(articleEntity);
    }

    @Override
    public ArticleEntity saveAndFlush(ArticleEntity articleEntity) {
        return articleJpaRepository.saveAndFlush(articleEntity);
    }

    @Override
    public List<ArticleEntity> saveAll(List<ArticleEntity> articleEntities) {
        return articleJpaRepository.saveAll(articleEntities);
    }

    @Override
    public void delete(ArticleEntity articleEntity) {
        articleJpaRepository.delete(articleEntity);
    }

    @Override
    public Optional<ArticleEntity> findById(Long articleId) {
        return articleJpaRepository.findById(articleId);
    }

    @Override
    public List<ArticleEntity> findAll() {
        return articleJpaRepository.findAll();
    }

    @Override
    public boolean isVisibleArticle(Long articleId) {
        return articleJpaRepository.existsByIdAndDeletedAtIsNullAndDisclosure(
                articleId,
                DisclosureType.VISIBLE
        );
    }

    @Override
    public List<ArticleEntity> findVisibleArticlesByOwner(UserEntity ownerEntity) {
        return articleJpaRepository.findVisibleArticlesByOwner(
                ownerEntity,
                DisclosureType.VISIBLE
        );
    }

    @Override
    public List<ArticleEntity> findNotDeletedArticlesByOwner(UserEntity ownerEntity) {
        return articleJpaRepository.findNotDeletedArticlesByOwner(ownerEntity);
    }

    @Override
    public Page<ArticleEntity> findDeletedArticlesByOwner(UserEntity ownerEntity, Pageable pageable) {
        return articleJpaRepository.findAllByOwnerEntityAndDeletedAtIsNotNull(ownerEntity, pageable);
    }

    @Override
    public List<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity
    ) {
        return articleJpaRepository.findReadableArticlesInConstellation(
                constellationEntity,
                DisclosureType.VISIBLE,
                userEntity
        );
    }

    @Override
    public Page<ArticleEntity> findReadableArticlesInConstellation(
            ConstellationEntity constellationEntity,
            UserEntity userEntity,
            Pageable pageable
    ) {
        return articleJpaRepository.findReadableArticlesInConstellation(
                constellationEntity,
                DisclosureType.VISIBLE,
                userEntity,
                pageable
        );
    }

    @Override
    public List<ArticleEntity> findArticlesInConstellation(ConstellationEntity constellationEntity) {
        return articleJpaRepository.findByConstellationEntity(constellationEntity);
    }

    @Override
    public List<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity) {
        return articleJpaRepository.findUnassignedArticlesByOwner(ownerEntity);
    }

    @Override
    public Page<ArticleEntity> findUnassignedArticlesByOwner(UserEntity ownerEntity, Pageable pageable) {
        return articleJpaRepository.findUnassignedArticlesByOwner(ownerEntity, pageable);
    }

    @Override
    public Integer countNotDeletedArticlesByOwner(UserEntity ownerEntity) {
        return articleJpaRepository.countByOwnerEntityAndDeletedAtIsNull(ownerEntity);
    }
}
