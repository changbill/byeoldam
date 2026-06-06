package com.ssafy.star.search.application;

import com.ssafy.star.article.dao.ArticleRepository;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.dto.Contour;
import com.ssafy.star.contour.dto.ContourResponse;
import com.ssafy.star.contour.repository.ContourRepository;
import com.ssafy.star.search.dao.ConstellationSearchRepository;
import com.ssafy.star.search.dto.response.ConstellationSearchResponse;
import com.ssafy.star.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Transactional
@Service
public class ConstellationSearchService {
    private final ConstellationSearchRepository constellationSearchRepository;
    private final ContourRepository contourRepository;
    private final ConstellationUserRepository constellationUserRepository;
    private final ArticleRepository articleRepository;

    int pageNumber = 0;
    int pageSize = 5;
    Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    @Transactional
    public ContourEntity findById(Long contourId) {
        return contourRepository.findById(contourId).orElseThrow(() ->
                new ByeolDamException(ErrorCode.CONTOUR_NOT_FOUND));
    }

    @Transactional
    public Page<ConstellationEntity> constellationSearch(String keyword, Pageable pageable) {
        return constellationSearchRepository.findAllByNameContaining(keyword, pageable);
    }

    @Transactional
    public Page<ConstellationEntity> constellationRelatedSearch(String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return constellationSearch(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ConstellationSearchResponse> constellationSearchResponses(
            String keyword,
            UserEntity viewer,
            Pageable pageable
    ) {
        Page<ConstellationEntity> page = constellationSearch(keyword, pageable);
        return new PageImpl<>(
                toConstellationSearchResponses(page.getContent(), viewer),
                pageable,
                page.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public Page<ConstellationSearchResponse> constellationRelatedSearchResponses(String keyword, UserEntity viewer) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return constellationSearchResponses(keyword, viewer, pageable);
    }

    private List<ConstellationSearchResponse> toConstellationSearchResponses(
            List<ConstellationEntity> constellationEntities,
            UserEntity viewer
    ) {
        if (constellationEntities.isEmpty()) {
            return List.of();
        }

        Map<Long, ContourEntity> contourById = StreamSupport.stream(
                        contourRepository.findAllById(
                                constellationEntities.stream()
                                        .map(ConstellationEntity::getContourId)
                                        .toList()
                        ).spliterator(),
                        false
                )
                .collect(Collectors.toMap(ContourEntity::get_id, Function.identity()));

        Map<Long, UserEntity> adminByConstellationId = constellationUserRepository
                .findAdminUsersByConstellationEntityIn(constellationEntities)
                .stream()
                .collect(Collectors.toMap(
                        constellationUser -> constellationUser.getConstellationEntity().getId(),
                        ConstellationUserEntity::getUserEntity
                ));

        Map<Long, Long> articleCountByConstellationId = articleRepository.countReadableArticlesByConstellations(
                constellationEntities,
                viewer
        );

        return constellationEntities.stream()
                .map(constellation -> {
                    ContourEntity contour = contourById.get(constellation.getContourId());
                    UserEntity admin = adminByConstellationId.get(constellation.getId());

                    return new ConstellationSearchResponse(
                            constellation.getId(),
                            constellation.getName(),
                            ContourResponse.fromContour(Contour.fromEntity(contour)),
                            constellation.getHits(),
                            admin == null ? null : admin.getNickname(),
                            articleCountByConstellationId.getOrDefault(constellation.getId(), 0L),
                            constellation.getCreatedAt(),
                            constellation.getModifiedAt()
                    );
                })
                .toList();
    }
}
