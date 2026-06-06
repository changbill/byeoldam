package com.ssafy.star.article.application;

import com.ssafy.star.article.dao.ArticleHashtagRelationRepository;
import com.ssafy.star.article.dao.ArticleLikeRepository;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.domain.ArticleHashtagEntity;
import com.ssafy.star.article.domain.ArticleHashtagRelationEntity;
import com.ssafy.star.article.domain.ArticleLikeEntity;
import com.ssafy.star.article.dto.ArticleDetail;
import com.ssafy.star.article.dto.ArticleSummary;
import com.ssafy.star.comment.dto.CommentDto;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.infra.S3.S3uploader;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.application.ImageService;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.image.dto.Image;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleHashtagRelationRepository articleHashtagRelationRepository;
    private final ConstellationRepository constellationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final S3uploader s3uploader;
    private final ImageService imageService;
    private final ArticleHashtagRelationService articleHashtagRelationService;
    private final ArticleLikeRepository articleLikeRepository;

    /**
     * 게시물 등록과 별자리 배정
      */
    @Transactional
    public void create(
            String title,
            String description,
            DisclosureType disclosureType,
            String email,
            MultipartFile imageFile,
            ImageType imageType,
            Set<String> articleHashtagSet,
            Long constellationId
    ) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId); // 배정하려는 별자리 Entity

        validateConstellationAdmin(constellationEntity, userEntity);
        saveArticleWithImage(title, description, disclosureType, userEntity, constellationEntity, imageFile, imageType, articleHashtagSet);
    }

    /**
     * 게시물 등록 및 미분류 별자리 배정
     */
    @Transactional
    public void createWithNoConstellation(
            String title,
            String description,
            DisclosureType disclosureType,
            String email,
            MultipartFile imageFile,
            ImageType imageType,
            Set<String> articleHashtagSet
    ) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        saveArticleWithImage(title, description, disclosureType, userEntity, null, imageFile, imageType, articleHashtagSet);
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public void modify(
            Long articleId,
            String title,
            String description,
            DisclosureType disclosure,
            String email,
            Set<String> articleHashtagSet
    ) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);

        articleHashtagRelationService.deleteByArticleEntity(articleEntity);
        articleHashtagRelationService.saveHashtag(articleEntity, articleHashtagSet);

        articleEntity.update(title, description, disclosure);
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void delete(Long articleId, String email) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);
        validateArticleNotDeleted(articleEntity, "article %d has already deleted");

        articleLikeRepository.deleteAllByArticleEntity(articleEntity);
        articleRepository.delete(articleEntity);
        articleHashtagRelationService.deleteByArticleEntity(articleEntity);
    }

    /**
     * 휴지통 조회
     */
    @Transactional
    public Page<ArticleSummary> trashcan(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.findDeletedArticlesByOwner(userEntity, pageable).map(ArticleSummary::fromEntity);
    }

    /**
     * 휴지통에 있는 게시물 복원
     */
    @Transactional
    public ArticleDetail undoDeletion(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        if(!articleEntity.getOwnerEntity().equals(userEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", userEntity.getNickname()));
        }

        validateArticleDeleted(articleEntity);

        articleEntity.undoDeletion();
        articleLikeRepository.findAllByArticleEntity(articleEntity).forEach(ArticleLikeEntity::undoDeletion);
        articleHashtagRelationRepository.findAllByArticleEntity(articleEntity).forEach(ArticleHashtagRelationEntity::undoDeletion);

        return getArticleDetail(articleEntity);
    }

    // 팔로우한 사람들의 게시물들을 최신순으로 나열해서 보여준다
    @Transactional(readOnly = true)
    public Page<ArticleSummary> followFeed(String email, Pageable pageable) {
        UserEntity viewerEntity = getUserEntityOrExceptionByEmail(email);

        return articleRepository
                .findFollowFeedLatestSort(viewerEntity, ApprovalStatus.ACCEPT, pageable)
                .map(ArticleSummary::fromEntity);
    }

    /**
     * 유저의 게시물 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<ArticleSummary> userArticlePage(String nickname, String email, Pageable pageable) {
        UserEntity viewerEntity = getUserEntityOrExceptionByEmail(email);
        UserEntity ownerEntity = getUserEntityOrExceptionByNickname(nickname);
        boolean visibleOnly = !viewerEntity.equals(ownerEntity)
                && followRepository.findByFromUserAndToUserAndStatus(
                        viewerEntity,
                        ownerEntity,
                        ApprovalStatus.ACCEPT
                ).isEmpty();

        return articleRepository.findArticlesByOwner(ownerEntity, visibleOnly, pageable)
                .map(ArticleSummary::fromEntity);
    }

    /**
     * 게시물 상세 조회
     */
    @Transactional(readOnly = true)
    public ArticleDetail detail(Long articleId, String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        ArticleEntity articleEntity = getArticleDetailEntityOrException(articleId);

        if(userEntity.getId() == articleEntity.getOwnerEntity().getId() || articleRepository.isVisibleArticle(articleId)) {
            articleEntity.addHits();

            return getArticleDetail(articleRepository.saveAndFlush(articleEntity));
        } else {
            validateArticleNotDeleted(articleEntity, "article %d has deleted");

            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with article %d", userEntity.getNickname(), articleId));
        }
    }

    /**
     * 별자리에 게시물 배정
     */
    @Transactional
    public void select(Long constellationId, Set<Long> articleIdSet, String email) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);                      // 현재 사용자 user entity
        ConstellationEntity constellationEntity = null;
        if(constellationId != -1) {
             constellationEntity = getConstellationEntityOrException(constellationId); // 배정하려는 별자리 Entity

            validateConstellationAdmin(constellationEntity, userEntity);
        } else {}

        // 반복문을 통해 Set에 있는 article 전부 별자리에 배정
        for(Long articleId : articleIdSet) {
            ArticleEntity articleEntity = getArticleEntityOrException(articleId);
            UserEntity ownerEntity = articleEntity.getOwnerEntity();                  // admin의 user entity

            validateArticleNotDeleted(articleEntity, "article %d deleted");

            // article 본인 것이 아니라면 예외처리
            if(ownerEntity != userEntity) {
                throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                        String.format("%s has no permission with %d", userEntity.getNickname(), articleId));
            }

            articleEntity.selectConstellation(constellationEntity);
        }
    }

    /**
     * 별자리의 전체 게시물 조회
     */
    @Transactional
    public Page<ArticleSummary> articlesInConstellation(Long constellationId, String email, Pageable pageable) {
        // email로 userEntity 구하고 별자리 공개여부와 해당 게시물 공유여부를 확인해 Error 반환
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);
        return articleRepository.findReadableArticlesInConstellation(constellationEntity, userEntity, pageable)
                .map(ArticleSummary::fromEntity);
    }

    /**
     * 미분류 별자리의 전체 게시물 조회
     */
    @Transactional
    public Page<ArticleSummary> articlesInNoConstellation(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.findUnassignedArticlesByOwner(userEntity, pageable)
                .map(ArticleSummary::fromEntity);
    }

    @Transactional
    public void like(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        // 좋아요 상태인지 확인
        articleLikeRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).ifPresentOrElse(
                articleLikeRepository::delete,
                () -> articleLikeRepository.save(ArticleLikeEntity.of(userEntity, articleEntity))
        );
    }
    @Transactional
    public Boolean checkLike(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);

        //좋아요 상태인지 확인
        return articleLikeRepository.findByUserEntityAndArticleEntity(userEntity, articleEntity).isPresent();
    }

    @Transactional
    public Integer likeCount(Long articleId) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);

        //좋아요 갯수 확인
        return articleLikeRepository.countByArticleEntity(articleEntity);
    }

    @Transactional(readOnly = true)
    public int countArticles(String email){
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);
        return articleRepository.countNotDeletedArticlesByOwner(userEntity);
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("article %d not founded", articleId)));
    }

    private ArticleEntity getArticleDetailEntityOrException(Long articleId) {
        return articleRepository.findDetailById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("article %d not founded", articleId)));
    }

    // 유저가 존재하는지(email)
    private UserEntity getUserEntityOrExceptionByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("email %s not founded", email)));
    }

    // 유저가 존재하는지(nickname)

    private UserEntity getUserEntityOrExceptionByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", nickname)));
    }

    // 별자리가 존재하는지

    private ConstellationEntity getConstellationEntityOrException(Long constellationId) {
        return constellationRepository.findById(constellationId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONSTELLATION_NOT_FOUND, String.format("constellation %d not founded", constellationId)));
    }

    private void saveArticleWithImage(
            String title,
            String description,
            DisclosureType disclosureType,
            UserEntity userEntity,
            ConstellationEntity constellationEntity,
            MultipartFile imageFile,
            ImageType imageType,
            Set<String> articleHashtagSet
    ) {
        String url = "";
        String thumbnailUrl = "";

        // 이미지 S3에 업로드 (가장 마지막에 놓을 것)
        try{
            url = s3uploader.upload(imageFile, "articles");
            thumbnailUrl = s3uploader.uploadThumbnail(imageFile, "thumbnails");
            ImageEntity imageEntity = imageService.saveImage(imageFile.getOriginalFilename(), url, thumbnailUrl, imageType);
            ArticleEntity articleEntity = ArticleEntity.of(
                    title,
                    description,
                    disclosureType,
                    userEntity,
                    constellationEntity,
                    imageEntity
            );

            articleRepository.save(articleEntity);
            articleHashtagRelationService.saveHashtag(articleEntity, articleHashtagSet);
        } catch (IOException e) {
            s3uploader.deleteImageFromS3(url);
            s3uploader.deleteImageFromS3(thumbnailUrl);
        }
    }

    private void validateConstellationAdmin(ConstellationEntity constellationEntity, UserEntity userEntity) {
        if(constellationEntity.getAdminEntity() != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with constellation %d",
                            userEntity.getNickname(),
                            constellationEntity.getId()));
        }
    }

    private void validateArticleNotDeleted(ArticleEntity articleEntity, String messageFormat) {
        if(articleEntity.getDeletedAt() != null) {
            throw new ByeolDamException(ErrorCode.ARTICLE_DELETED,
                    String.format(messageFormat, articleEntity.getId()));
        }
    }
    private void validateArticleDeleted(ArticleEntity articleEntity) {
        if(articleEntity.getDeletedAt() == null) {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST,
                    String.format("article %d is not abandoned", articleEntity.getId()));
        }
    }

    // 게시물 owner인지 확인
    private ArticleEntity getArticleOwnerOrException(Long articleId, String email){
        UserEntity userEntity = getUserEntityOrExceptionByEmail(email);                       // 현재 사용자 user entity
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity ownerEntity = articleEntity.getOwnerEntity();                              // admin의 user entity

        // admin이어야 삭제 가능
        if(ownerEntity != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %d", userEntity.getNickname(), articleId));
        }

        return articleEntity;
    }

    private ArticleDetail getArticleDetail(ArticleEntity entity) {
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
