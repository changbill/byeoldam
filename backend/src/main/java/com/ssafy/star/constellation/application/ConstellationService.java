package com.ssafy.star.constellation.application;

import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.article.dto.HoverArticle;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.infra.S3.S3uploader;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.constellation.ConstellationUserRole;
import com.ssafy.star.constellation.dao.ConstellationLikeRepository;
import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationLikeEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.constellation.dto.ConstellationUser;
import com.ssafy.star.constellation.dto.ConstellationWithArticle;
import com.ssafy.star.constellation.dto.response.ConstellationForUserResponse;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.dto.Contour;
import com.ssafy.star.contour.repository.ContourRepository;
import com.ssafy.star.image.ImageType;
import com.ssafy.star.image.application.ImageService;
import com.ssafy.star.image.dao.ImageRepository;
import com.ssafy.star.image.domain.ImageEntity;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ssafy.star.constellation.ConstellationUserRole.ADMIN;
import static com.ssafy.star.constellation.ConstellationUserRole.USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConstellationService {

    private final ArticleRepository articleRepository;
    private final ConstellationRepository constellationRepository;
    private final ConstellationUserRepository constellationUserRepository;
    private final UserRepository userRepository;
    private final ContourRepository contourRepository;
    private final FollowRepository followRepository;
    private final S3uploader s3uploader;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ConstellationLikeRepository constellationLikeRepository;

    /**
     * 나의 우주 보기 - 별자리 전체 조회
     * VISIBLE, INVISIBLE 상관 없이, 개인 별자리, 공유 별자리 확인
     * 내부 게시물은 deletedAt NULL인 경우 확인 가능
     */
    @Transactional(readOnly = true)
    public Page<ConstellationWithArticle> myConstellations(String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityByEmailOrException(email);
        Page<ConstellationEntity> page = constellationRepository.findAllByUserEntity(userEntity, pageable);
        return toConstellationWithArticlePage(page, userEntity, pageable);
    }

    /**
     * 다른 유저의 우주 보기 - 별자리 전체 조회
     * - 내가 다른 사람의 우주에 접근했을 때
     */
    @Transactional(readOnly = true)
    public Page<ConstellationWithArticle> userConstellations(String nickname, String email, Pageable pageable) {
        UserEntity userEntity = getUserEntityByNicknameOrException(nickname);  // 타 유저의 계정 이메일
        UserEntity myEntity = getUserEntityByEmailOrException(email);      // 로그인한 사람의 이메일
        // 내 계정으로 내 우주를 접근하는 경우
        if (userEntity.equals(myEntity)) {
            return myConstellations(email, pageable);
        }

        if (DisclosureType.INVISIBLE == userEntity.getDisclosureType()) {
            // 나와 팔로우 관계라면 가능
            followRepository.findByFromUserAndToUserAndStatus(myEntity, userEntity, ApprovalStatus.ACCEPT)
                    .orElseThrow(() -> new ByeolDamException(ErrorCode.INVALID_PERMISSION));
        }

        Page<ConstellationEntity> page = constellationUserRepository.findConstellationByUserEntity(userEntity, pageable);
        return toConstellationWithArticlePage(page, userEntity, pageable);
    }

    // 별자리에 공유할 유저 추가
    @Transactional
    public void addUser(Long constellationId, String nickname, String email) {
        // user 존재하는지 확인
        UserEntity userEntity = getUserEntityByNicknameOrException(nickname);

        // 사용자가 admin인지 확인
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);

        // 이미 공유 별자리에 포함된 상태라면
        if (constellationUserRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity).isPresent()) {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("%s has already added", nickname));
        }

        // 별자리에 공유할 유저 추가, 권한은 유저로
        ConstellationUserEntity constellationUserEntity = ConstellationUserEntity.of(constellationEntity, userEntity, USER);
        constellationEntity.addUser(constellationUserEntity);

        constellationUserRepository.saveAndFlush(constellationUserEntity);
    }

    /**
     * 별자리 공유하는 유저 강퇴
     */
    @Transactional
    public void deleteUser(Long constellationId, String nickname, String email) {
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);
        UserEntity myEntity = getUserEntityByEmailOrException(email);

        // Admin 본인 별자리회원 연관관계는 삭제 불가
        UserEntity userEntity = getUserEntityByNicknameOrException(nickname);
        if (myEntity.equals(userEntity)) {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, "you cannot delete yourself");
        }

        // userEntity가 constellationEntity에 속하는지
        List<ConstellationUserEntity> constellationUserEntities = constellationUserRepository
                .findByConstellationEntity(constellationEntity, Pageable.unpaged())
                .getContent();
        List<UserEntity> userEntities = constellationUserEntities.stream().map(ConstellationUserEntity::getUserEntity).toList();

        for (UserEntity user : userEntities) {
            if (user.equals(userEntity)) {
                // 삭제
                ConstellationUserEntity constellationUserEntity = constellationUserRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity)
                        .orElseThrow(() ->
                                new ByeolDamException(ErrorCode.INVALID_REQUEST, "wrong gateway"));
                constellationEntity.deleteUser(constellationUserEntity);

                constellationUserRepository.saveAndFlush(constellationUserEntity);
                return;
            }
        }

        throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("%s not belongs to %s", "userEmail:" + nickname, "constellationName:" + constellationEntity.getName()));
    }

    /**
     * 공유 별자리 유저 조회
     */
    @Transactional(readOnly = true)
    public Page<ConstellationForUserResponse> findConstellationUsers(Long constellationId, Pageable pageable) {
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        return constellationUserRepository.findByConstellationEntity(constellationEntity, pageable)
                .map(this::toConstellationForUserResponse);
    }

    // 관리자와 유저 UserRole 맞바꾸기

    @Transactional
    public void roleModify(Long constellationId, String nickname, String email) {
        // user 존재하는지 확인
        UserEntity userEntity = getUserEntityByNicknameOrException(nickname);
        UserEntity myEntity = getUserEntityByEmailOrException(email);

        // 사용자가 admin이라면
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);

        // 대상 user, ADMIN으로 권한 변경
        changeRole(userEntity, constellationEntity, ADMIN);

        // ADMIN, USER로 권한 변경
        try {
            // 도중 오류 발생 시 user 권한 되돌리기
            changeRole(myEntity, constellationEntity, USER);
        } catch (ByeolDamException e) {
            changeRole(userEntity, constellationEntity, USER);
        }
    }

    /**
     * 2. 별자리 추가(생성) -완료
     * - 사진 3장(원본, 썸네일, 윤곽선) S3 업로드 -> 사진 각각 String url 반환
     * - key : origin(원본), thumb(썸네일), cthumb(썸네일 + 윤곽선)
     * - 사진들을 이미지 테이블에 저장
     * - 몽고 DB 저장(사진 url, 윤곽선 리스트들, 선택된 윤곽선 리스트 하나) -> 저장된 몽고 DB  ID 반환
     * - 몽고DB의 ID 값을 별자리 테이블에 id 값 저장
     */
    @Transactional
    public void create(
            String email,
            String name,
            MultipartFile origin,
            MultipartFile thumb,
            MultipartFile cthumb,
            List<List<List<Integer>>> contoursList,
            List<List<Integer>> ultimate
    ) throws IOException {
        // 사용자의 user 엔터티 가져오기
        UserEntity userEntity = getUserEntityByEmailOrException(email);

        // 사진들 추가
        String originUrl = s3uploader.upload(origin, "constellation/origin");
        String thumbUrl = s3uploader.upload(thumb, "constellation/thumb");
        String cThumbUrl = s3uploader.upload(cthumb, "constellation/cthumb");

        // image 테이블에 저장
        imageService.saveImage(origin.getOriginalFilename(), originUrl, null, ImageType.CONSTELLATION);
        imageService.saveImage(thumb.getOriginalFilename(), thumbUrl, null, ImageType.CONSTELLATION);
        imageService.saveImage(cthumb.getOriginalFilename(), cThumbUrl, null, ImageType.CONSTELLATION);

        // 몽고 DB에 저장
        ContourEntity contour = contourRepository.save(ContourEntity.of(originUrl, thumbUrl, cThumbUrl, contoursList, ultimate));

        // 별자리 엔터티 생성
        ConstellationEntity constellationEntity = ConstellationEntity.of(
                name
        );
        // mongo에 저장된 id 반환
        constellationEntity.setContourId(contour.get_id());
        ConstellationUserEntity constellationUserEntity = ConstellationUserEntity.of(
                constellationEntity,
                userEntity,
                ConstellationUserRole.ADMIN
        );
        constellationUserRepository.save(constellationUserEntity);

        // 별자리를 데이터베이스에 저장
        constellationRepository.saveAndFlush(constellationEntity);
    }

    /**
     * 3. 별자리 삭제 - 완료
     * - 별자리 Id 값 받아 조회하기
     * - 몽고DB 조회해서 사진 url 찾기(3개)
     * - S3에서 사진 삭제
     * - 몽고 DB 데이터 삭제
     * - 별자리에 포함되어 있는 별들의 별자리를 미분류로 변환
     * - 별자리 정보를 마리아 DB에서 삭제
     */
    @Transactional
    public void deleteConstellationWithContour(String email, Long constellationId) {
        // 사용자가 해당 별자리 admin인지 확인
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);
        UserEntity userEntity = getUserEntityByEmailOrException(email);
        constellationLikeRepository.deleteAllByConstellationEntity(constellationEntity);
        Long contourId = constellationEntity.getContourId();
        System.out.println("contourId : " + contourId);

        ContourEntity contourEntity = contourRepository.findById(contourId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND)
        );

        // 몽고DB에서 contour 삭제
        contourRepository.delete(contourEntity);

        // 별자리의 별들을 미분류로 변환
        List<ArticleEntity> articleEntities = articleRepository
                .findArticlesInConstellation(constellationEntity, Pageable.unpaged())
                .getContent()
                .stream()
                .map(article -> {
                    article.selectConstellation(null);
                    return article;
                }).collect(Collectors.toList());

        articleRepository.saveAll(articleEntities);

        // 별자리 삭제
        constellationRepository.delete(constellationEntity);

        ImageEntity origin = imageService.getImageUrl(contourEntity.getOriginUrl());
        ImageEntity thumb = imageService.getImageUrl(contourEntity.getThumbUrl());
        ImageEntity cThumb = imageService.getImageUrl(contourEntity.getCThumbUrl());
        // 이미지 테이블에서 관련 이미지 삭제
        imageRepository.delete(origin);
        imageRepository.delete(thumb);
        imageRepository.delete(cThumb);

        // S3에서 사진 삭제
        s3uploader.deleteImageFromS3(contourEntity.getOriginUrl());
        s3uploader.deleteImageFromS3(contourEntity.getThumbUrl());
        s3uploader.deleteImageFromS3(contourEntity.getCThumbUrl());
    }


    //
    /**
     * 4. 수정요청 로직시 - 현재 윤곽선 정보 반환 - 완료
     * - 별자리 조회
     * - 몽고DB ID 조회
     * - 몽고 DB에서 원본 사진 하나 url
     * - 전체 윤곽선 리스트들
     * - 최종 윤곽선 데이터
     * - 프론트에 원본 사진 띄어주기 (원본 사진 위에 윤곽선이 표시가 돼)
     */
    @Transactional(readOnly = true)
    public Contour requestModifyConstellation(String email, Long constellationId) {
        // 별자리 조회
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);
        Long contourId = constellationEntity.getContourId();
        // 몽고DB조회해서 반환
        return Contour.fromEntity(
                contourRepository.findById(contourId)
                        .orElseThrow(() ->
                                new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND, String.format("ContourId : %s is not founded", contourId))
                        )
        );
    }

    /**
     * 5. 별자리 수정확정 로직
     * - 리퀘스트로 사진 3장 받기 + 윤곽선 리스트들 + 선택된 윤곽선 -
     * - S3에 이미지 저장 -
     * - 기존 별자리 조회 -> 몽고 DB id 조회 -> 사진 url 3장 가져오기
     * - S3에서 해당 이미지 삭제하기
     * - 이미지 테이블 엔티티 삭제
     * - 위의 S3이미지로 이미지 테이블에 추가 -> Constellation
     * - 몽고DB에서 해당 id로 들어가 값을 수정
     * - 별자리 이름, 태그 등 변경 내용이 있으면 수정
     */
    @Transactional
    public void modify(
            String email,                // 사용자의 email
            Long constellationId,
            String name,
            MultipartFile origin,
            MultipartFile thumb,
            MultipartFile cThumb,
            List<List<List<Integer>>> contoursList,
            List<List<Integer>> ultimate
    ) throws IOException {
        // 별자리 가져오기(나의 별자리 범주이고, 내가 Admin인 경우)
        ConstellationEntity constellationEntity = getConstellationEntityIfAdminOrException(constellationId, email);

        // 사진들 추가
        String originUrl = s3uploader.upload(origin, "constellation/origin");
        String thumbUrl = s3uploader.upload(thumb, "constellation/thumb");
        String cThumbUrl = s3uploader.upload(cThumb, "constellation/cthumb");

        Long contourId = constellationEntity.getContourId();
        ContourEntity contourEntity = contourRepository.findById(contourId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND, String.format("ContourId : %s is not founded", contourId))
        );

        // 기존 이미지 URL
        String oldOriginUrl = contourEntity.getOriginUrl();
        String oldThumbUrl = contourEntity.getThumbUrl();
        String oldCThumbUrl = contourEntity.getCThumbUrl();

        ImageEntity oldOriginImage = imageService.getImageUrl(oldOriginUrl);
        log.info("oldOriginImage : {}", oldOriginImage);
        ImageEntity oldThumbImage = imageService.getImageUrl(oldThumbUrl);
        log.info("oldThumbImage : {}", oldThumbImage);
        ImageEntity oldContourThumbImage = imageService.getImageUrl(oldCThumbUrl);
        log.info("oldContourThumbImage : {}", oldContourThumbImage);
        // 기존 이미지 삭제
        s3uploader.deleteImageFromS3(oldOriginUrl);
        s3uploader.deleteImageFromS3(oldThumbUrl);
        s3uploader.deleteImageFromS3(oldCThumbUrl);

        // 몽고DB에 반영하기
        contourRepository.delete(contourEntity);
//        contourEntity.setOriginUrl(originUrl);
//        contourEntity.setThumbUrl(thumbUrl);
//        contourEntity.setCThumbUrl(cThumbUrl);
//        contourEntity.setContoursList(contoursList);
//        contourEntity.setUltimate(ultimate);

        ContourEntity newContourEntity = contourRepository.save(ContourEntity.of(originUrl, thumbUrl, cThumbUrl, contoursList, ultimate));
        //contourRepository.delete(contourEntity);
        //constellationEntity.setContourId(newContourEntity.getId());
        constellationEntity.setContourId(newContourEntity.get_id());

        oldOriginImage.setName(origin.getOriginalFilename());
        oldOriginImage.setUrl(originUrl);
        imageRepository.save(oldOriginImage);

        oldThumbImage.setName(thumb.getOriginalFilename());
        oldThumbImage.setUrl(thumbUrl);
        imageRepository.save(oldOriginImage);

        oldContourThumbImage.setName(cThumb.getOriginalFilename());
        oldContourThumbImage.setUrl(cThumbUrl);
        imageRepository.save(oldContourThumbImage);

        if (name != null) {
            constellationEntity.setName(name);
        }
        constellationRepository.save(constellationEntity);
    }


    private Page<ConstellationWithArticle> toConstellationWithArticlePage(
            Page<ConstellationEntity> page,
            UserEntity userEntity,
            Pageable pageable
    ) {
        return new PageImpl<>(
                toConstellationWithArticles(page.getContent(), userEntity),
                pageable,
                page.getTotalElements()
        );
    }

    private ConstellationForUserResponse toConstellationForUserResponse(ConstellationUserEntity constellationUserEntity) {
        UserEntity userEntity = constellationUserEntity.getUserEntity();
        String imageUrl = null;
        if(userEntity.getImageEntity() != null) {
            imageUrl = userEntity.getImageEntity().getUrl();
        }
        return new ConstellationForUserResponse(
                imageUrl,
                userEntity.getName(),
                userEntity.getNickname(),
                constellationUserEntity.getConstellationUserRole()
        );
    }

    private List<ConstellationWithArticle> toConstellationWithArticles(
            List<ConstellationEntity> constellationEntities,
            UserEntity userEntity
    ) {
        if (constellationEntities.isEmpty()) {
            return List.of();
        }

        Map<Long, ContourEntity> contourById = contourRepository.findAllById(
                        constellationEntities.stream()
                                .map(ConstellationEntity::getContourId)
                                .toList()
                )
                .stream()
                .collect(Collectors.toMap(ContourEntity::get_id, Function.identity()));

        Map<Long, List<ConstellationUser>> usersByConstellationId = constellationUserRepository
                .findConstellationUserEntitiesByConstellationEntityIn(constellationEntities)
                .stream()
                .collect(Collectors.groupingBy(
                        constellationUserEntity -> constellationUserEntity.getConstellationEntity().getId(),
                        Collectors.mapping(ConstellationUser::fromEntity, Collectors.toList())
                ));

        Map<Long, List<HoverArticle>> articlesByConstellationId = articleRepository
                .findReadableArticlesInConstellations(constellationEntities, userEntity)
                .stream()
                .collect(Collectors.groupingBy(
                        articleEntity -> articleEntity.getConstellationEntity().getId(),
                        Collectors.mapping(HoverArticle::fromEntity, Collectors.toList())
                ));

        return constellationEntities.stream()
                .map(constellationEntity -> {
                    ContourEntity contourEntity = contourById.get(constellationEntity.getContourId());
                    if (contourEntity == null) {
                        throw new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND);
                    }

                    return new ConstellationWithArticle(
                            constellationEntity.getId(),
                            constellationEntity.getName(),
                            Contour.fromEntity(contourEntity),
                            constellationEntity.getHits(),
                            usersByConstellationId.getOrDefault(constellationEntity.getId(), List.of()),
                            constellationEntity.getCreatedAt(),
                            constellationEntity.getModifiedAt(),
                            articlesByConstellationId.getOrDefault(constellationEntity.getId(), List.of())
                    );
                })
                .toList();
    }

    // 유저가 존재하는지 확인(nickname)
    private UserEntity getUserEntityByNicknameOrException(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", nickname)));
    }

    // 유저가 존재하는지 확인(email)
    private UserEntity getUserEntityByEmailOrException(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
    }

    // 별자리가 존재하는지 확인
    private ConstellationEntity getConstellationEntityOrException(Long constellationId) {
        return constellationRepository.findById(constellationId)
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.CONSTELLATION_NOT_FOUND, String.format("constellation %d has not founded", constellationId)));
    }

    // 사용자가 admin인지 확인 - 별자리 생성한 사람이 email로 받은 유저와 동일한지
    private ConstellationEntity getConstellationEntityIfAdminOrException(Long constellationId, String email) {
        UserEntity userEntity = getUserEntityByEmailOrException(email);                                                            // 현재 사용자 user entity
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);
        UserEntity adminEntity = constellationEntity.getAdminEntity();

        if (adminEntity != null) {
            // admin이어야 삭제 가능
            if (!adminEntity.equals(userEntity)) {
                throw new ByeolDamException(ErrorCode.INVALID_PERMISSION,
                        String.format("%s has no permission with constellation %d", userEntity.getNickname(), constellationId));
            }
            return constellationEntity;
        } else {
            throw new ByeolDamException(ErrorCode.INVALID_REQUEST, String.format("%d has no admin. it's very dangerous", constellationId));
        }
    }

    private void changeRole(UserEntity userEntity, ConstellationEntity constellationEntity, ConstellationUserRole role) {
        // role 데이터를 저장하고 있는 별자리회원 Entity
        ConstellationUserEntity constellationUserEntity = constellationUserRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity)
                .orElseThrow(() ->
                        new ByeolDamException(ErrorCode.CONSTELLATION_USER_NOT_FOUND, String.format("%s, constellation %s has no constellationUserEntity", userEntity.getNickname(), constellationEntity.getId())));
        constellationUserEntity.setConstellationUserRole(role);
        constellationUserRepository.saveAndFlush(constellationUserEntity);
    }

    //별자리 좋아요 요청
    @Transactional
    public void like(Long constellationId, String email) {
        UserEntity userEntity = getUserEntityByEmailOrException(email);                                                            // 현재 사용자 user entity
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        // 좋아요 상태인지 확인
        constellationLikeRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity).ifPresentOrElse(
                constellationLikeRepository::delete,
                () -> constellationLikeRepository.save(ConstellationLikeEntity.of(userEntity, constellationEntity))
        );
    }

    //별자리 좋아요 상태 확인
    @Transactional
    public Boolean checkLike(Long constellationId, String email) {
        UserEntity userEntity = getUserEntityByEmailOrException(email);                                                            // 현재 사용자 user entity
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        //좋아요 상태인지 확인
        return constellationLikeRepository.findByUserEntityAndConstellationEntity(userEntity, constellationEntity).isPresent();
    }

    //별자리 좋아요 갯수 확인
    @Transactional
    public Integer likeCount(Long constellationId) {
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);

        //좋아요 갯수 확인
        return constellationLikeRepository.countByConstellationEntity(constellationEntity);
    }

    //별자리 좋아요한 사람들의 목록 확인
    @Transactional
    public Page<User> likeList(Long constellationId, Pageable pageable) {
        ConstellationEntity constellationEntity = getConstellationEntityOrException(constellationId);
        return constellationLikeRepository.findAllByConstellationEntity(constellationEntity, pageable)
                .map(ConstellationLikeEntity::getUserEntity)
                .map(User::fromEntity);
    }

    public int countConstellations(String email){
        UserEntity userEntity = getUserEntityByEmailOrException(email);
        return constellationUserRepository.countConstellationByUser(userEntity);
    }
}
