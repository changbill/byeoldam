package com.ssafy.star.article.application;

import com.ssafy.star.article.dao.ArticleHashtagRelationRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleHashtagRelationService {
    private final ArticleHashtagService articleHashtagService;
    private final ArticleHashtagRelationRepository articleHashtagRelationRepository;

    public void saveHashtag(ArticleEntity articleEntity, Set<String> articleHashtagSet) {
        if(articleHashtagSet.size() == 0) return;
        articleHashtagSet.stream()
                .map(tagName ->
                        articleHashtagService.findByTagName(tagName)
                                .orElseGet(() -> articleHashtagService.save(tagName)))
                .forEach(tagName -> mapHashtagToArticle(articleEntity, tagName));
    }

    private ArticleHashtagRelationEntity mapHashtagToArticle(ArticleEntity articleEntity, ArticleHashtagEntity articleHashtagEntity) {
        return articleHashtagRelationRepository.save(ArticleHashtagRelationEntity.of(articleHashtagEntity, articleEntity));
    }

    public void deleteByArticleEntity(ArticleEntity articleEntity) {
        articleHashtagRelationRepository.deleteByArticleEntity(articleEntity);
    }

    public List<ArticleHashtagRelationEntity> findHashtagListByArticle(ArticleEntity articleEntity) {
        return articleHashtagRelationRepository.findAllByArticleEntity(articleEntity);
    }

    public Page<ArticleEntity> findAllByHashtag(int page, String tagName) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")));
        return articleHashtagRelationRepository.findArticlesByTagName(tagName, pageable);
    }
}
