package com.ssafy.star.article.application;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ConstellationRepository constellationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    /**
     * 게시물 등록
      */
    @Transactional
    public void create(
            String title,
            String tag,
            String description,
            DisclosureType disclosureType,
            String email
    ) {
        UserEntity userEntity = getUserEntityOrException(email);

        // constellationEntity가 null인 경우 별자리 미분류 게시물
        articleRepository.save(ArticleEntity.of(title, tag, description, disclosureType, userEntity, null));
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public Article modify(
            Long articleId,
            String title,
            String tag,
            String description,
            DisclosureType disclosure,
            String email
    ) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);

        articleEntity.setTitle(title);
        articleEntity.setTag(tag);
        articleEntity.setDescription(description);
        articleEntity.setDisclosure(disclosure);
        return Article.fromEntity(articleRepository.saveAndFlush(articleEntity));
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void delete(Long articleId, String email) {
        // 게시물 owner가 맞는지 확인
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);
        articleRepository.delete(articleEntity);
    }

    /**
     * 휴지통 조회
     */
    @Transactional
    public Page<Article> trashcan(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(email);
        return articleRepository.findAllByOwnerEntityAndDeleted(userEntity, pageable).map(Article::fromEntity);
    }

    /**
     * 휴지통에 있는 게시물 복원
     */
    @Transactional
    public Article undoDeletion(Long articleId, String email) {
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity userEntity = getUserEntityOrException(email);

        if(!articleEntity.getOwnerEntity().equals(userEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission", "email:" + email));
        }

        if(articleEntity.getDeletedAt() != null) {
            articleEntity.undoDeletion();
        } else {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("%s is not abandoned", "articleId:" + Long.toString(articleId)));
        }

        return Article.fromEntity(articleEntity);
    }

    /**
     * 게시물 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<Article> list(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(email);
        // Not deleted 상태이고, DisclosureType이 VISIBLE이거나 자신의 게시물이라면 보여주기
        return articleRepository.findAllByNotDeletedAndDisclosure(userEntity, pageable).map(Article::fromEntity);
    }

    /**
     * 유저의 게시물 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<Article> userArticlePage(String userEmail, String myEmail, Pageable pageable) {
        // 찾는 유저가 접속자라면 전체 조회한다
        UserEntity myEntity = getUserEntityOrException(myEmail);
        UserEntity userEntity = getUserEntityOrException(userEmail);
        if(!myEntity.equals(userEntity)) {

            // following 중이라면 전체 조회한다
            if(!followRepository.findByFromUserAndToUser(myEntity, userEntity).isPresent()) {

                // disclosureType에 따라 조회여부 판단
                return articleRepository.findAllByOwnerEntityAndNotDeletedAndDisclosure(userEntity, pageable).map(Article::fromEntity);
            }
        }
        return articleRepository.findAllByOwnerEntityAndNotDeleted(userEntity, pageable).map(Article::fromEntity);
    }

    /**
     * 게시물 상세 조회
     */
    @Transactional(readOnly = true)
    public Article detail(Long articleId, String email) {
        UserEntity userEntity = getUserEntityOrException(email);

        // 해당 article이 없을 경우 예외처리
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", "articleId:" + Long.toString(articleId))));

        // articleId AND deletedAt == null AND (내 게시물이거나 VISIBLE)
        if(articleRepository.findByArticleIdAndNotDeleted(articleId, userEntity)) {
            articleEntity.setHits(articleEntity.getHits() + 1);

            return Article.fromEntity(articleRepository.save(articleEntity));
        } else {
            // Deletion 예외처리
            if(articleEntity.getDeletedAt() != null) {
                throw new ByeolDamException(ErrorCode.ARTICLE_DELETED, String.format("%s deleted", "articleId:" + Long.toString(articleId)));
            }

            // 볼 수 있는 권한이 없다(내 게시물이 아니거나 INVISIBLE)
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", "email:"+email, "articleId:" + Long.toString(articleId)));
        }
    }

    /**
     * 별자리 배정 및 변경
     */
    @Transactional
    public void select(Long articleId, Long constellationId, String email) {
        ArticleEntity articleEntity = getArticleOwnerOrException(articleId, email);
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        // 선택한 별자리가 기존의 constellationEntity인 경우 Error 반환
        if(articleEntity.getConstellationEntity() == null) {

        } else if (articleEntity.getConstellationEntity().equals(constellationEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("%s is already constellation %s", "articleId:" + Long.toString(articleId), "constellationId:" + Long.toString(constellationId)));
        }
        articleEntity.setConstellationEntity(constellationEntity);
    }

    /**
     * 별자리의 전체 게시물 조회
     */
    @Transactional
    public Page<Article> articlesInConstellation(Long constellationId, String email, Pageable pageable) {
        // email로 userEntity 구하고 별자리 공개여부와 해당 게시물 공유여부를 확인해 Error 반환
        UserEntity userEntity = getUserEntityOrException(email);

        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        //TODO : 별자리가 NONSHARED
        return articleRepository.findAllByConstellationEntity(constellationEntity, userEntity, pageable).map(Article::fromEntity);
    }

    // 포스트가 존재하는지
    private ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not founded", "articleId:" + Long.toString(articleId))));
    }

    // 유저가 존재하는지
    private UserEntity getUserEntityOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", "email:" +email)));
    }

    // 별자리가 존재하는지
    private ConstellationEntity getConstellationEntityOrException(Long constellationId) {
        return constellationRepository.findById(constellationId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONSTELLATION_NOT_FOUND, String.format("%s not founded", "constellationId:" + Long.toString(constellationId))));
    }

    // 게시물 owner인지 확인
    private ArticleEntity getArticleOwnerOrException(Long articleId, String email){
        UserEntity userEntity = getUserEntityOrException(email);                                    // 현재 사용자 user entity
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);
        UserEntity ownerEntity = articleEntity.getOwnerEntity();                              // admin의 user entity

        // admin이어야 삭제 가능
        if(ownerEntity != userEntity) {
            throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", "email:"+email, "articleId:" + Long.toString(articleId)));
        }

        return articleEntity;
    }
}
