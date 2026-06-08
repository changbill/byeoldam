package com.ssafy.star.search.application;

import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.article.dto.ArticleDetail;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.image.dto.Image;
import com.ssafy.star.search.dao.ArticleSearchRepository;
import com.ssafy.star.user.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ssafy.star.common.types.DisclosureType.VISIBLE;


@RequiredArgsConstructor
@Transactional
@Service
public class ArticleSearchService {

    private final ArticleSearchRepository articleSearchRepository;

    int pageNumber = 0;
    int pageSize = 5;

    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    @Transactional
    public List<ArticleDetail> titleSearch(String keyword) {
        return articleSearchRepository.findByTitleContainingAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, sort).stream().map(articleEntity -> getArticleDetail(articleEntity)).collect(Collectors.toList());
    }

    @Transactional
    public Page<ArticleDetail> titleRelatedSearch(String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return articleSearchRepository.findAllByTitleContainingAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, pageable).map(articleEntity -> getArticleDetail(articleEntity));
    }

    @Transactional
    public List<ArticleDetail> hashtagSearch(String keyword) {
        return articleSearchRepository.findByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, sort).stream().map(articleEntity -> getArticleDetail(articleEntity)).collect(Collectors.toList());
    }

    @Transactional
    public Page<ArticleDetail> hashtagRelatedSearch(String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return articleSearchRepository.findAllByArticleHashtagRelationEntities_ArticleHashtagEntity_TagNameAndDisclosureAndDeletedAtIsNull(keyword, VISIBLE, pageable).map(articleEntity -> getArticleDetail(articleEntity));
    }

    public ArticleDetail getArticleDetail(ArticleEntity entity) {
        Set<String> hashtags = new HashSet<>();
        try{
            hashtags = entity.getArticleHashtagRelationEntities()
                    .stream()
                    .map(ArticleHashtagRelationEntity::getArticleHashtagEntity)
                    .map(ArticleHashtagEntity::getTagName)
                    .collect(Collectors.toSet());
        } catch(NullPointerException e) {
            hashtags = null;
        }

        List<CommentDto> comments = null;
        try {
            comments = entity.getCommentEntities()
                    .stream()
                    .map(CommentDto::from)
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            comments = null;
        }

        Constellation constellation = null;
        try{
            constellation = Constellation.fromEntity(entity.getConstellationEntity());
        } catch(NullPointerException e) {
            constellation = null;
        }

        return new ArticleDetail(
                entity.getId(),
                entity.getTitle(),
                entity.getHits(),
                entity.getDescription(),
                entity.getDisclosure(),
                hashtags,
                constellation,
                User.fromEntity(entity.getOwnerEntity()),
                comments,
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt(),
                Image.fromEntity(entity.getImageEntity())
        );
    }
}
