package com.ssafy.star.search.api;

import com.ssafy.star.article.dto.response.ArticleDetailResponse;
import com.ssafy.star.article.dto.response.Response;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.search.application.ArticleSearchService;
import com.ssafy.star.search.application.ConstellationSearchService;
import com.ssafy.star.search.application.UserSearchService;
import com.ssafy.star.search.dto.response.ConstellationSearchResponse;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.response.SearchResponse;
import com.ssafy.star.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleSearchService articleSearchService;
    private final ConstellationSearchService constellationSearchService;
    private final UserSearchService userSearchService;
    private final UserRepository userRepository;


    @Operation(
            summary = "제목 검색 기능",
            description = "제목 검색 기능입니다. " +
                    "게시물의 제목을 기준으로 게시물 리스트를 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/search/title")
    public Response<List<ArticleDetailResponse>> titleSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.titleSearch(keyword).stream().map(ArticleDetailResponse::fromArticleDetail).toList());
    }

    @Operation(
            summary = "제목 연관 검색 기능",
            description = "제목 연관 검색 기능입니다. " +
                    "게시물의 제목을 기준으로 게시물을 5개 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/related-search/title")
    public Response<List<ArticleDetailResponse>> titleRelatedSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.titleRelatedSearch(keyword).map(ArticleDetailResponse::fromArticleDetail).stream().toList());
    }

    @Operation(
            summary = "해시태그 검색 기능",
            description = "해시태그 검색 기능입니다. " +
                    "게시물의 해시태그를 기준으로 게시물을 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/search/hashtag")
    public Response<List<ArticleDetailResponse>> hashtagSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.hashtagSearch(keyword).stream().map(ArticleDetailResponse::fromArticleDetail).toList());
    }

    @Operation(
            summary = "해시태그 연관 검색 기능",
            description = "해시태그 연관 검색 기능입니다. " +
                    "게시물의 해시태그를 기준으로 게시물을 5개 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/related-search/hashtag")
    public Response<List<ArticleDetailResponse>> hashtagRelatedSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(articleSearchService.hashtagRelatedSearch(keyword).map(ArticleDetailResponse::fromArticleDetail).stream().toList());
    }

    @Operation(
            summary = "별자리 검색 기능",
            description = "별자리 검색 기능입니다. " +
            "별자리 name을 기준으로 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ConstellationSearchResponse.class)))
            }
    )
    @GetMapping("/search/constellation")
    public Response<Page<ConstellationSearchResponse>> constellationSearch(
            @RequestParam String keyword,
            Authentication authentication,
            Pageable pageable
    ) {
        log.info("request 정보 : {}", keyword);

        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND)
        );

        return Response.success(constellationSearchService.constellationSearchResponses(keyword, userEntity, pageable));
    }

    @Operation(
            summary = "별자리 연관 검색 기능",
            description = "별자리 연관 검색 기능입니다. " +
                    "별자리 name을 기준으로 5개 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ConstellationSearchResponse.class)))
            }
    )
    @GetMapping("/related-search/constellation")
    public Response<Page<ConstellationSearchResponse>> constellationRelatedSearch(
            @RequestParam String keyword,
            Authentication authentication
    ) {
        log.info("request 정보 : {}", keyword);

        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND)
        );

        return Response.success(constellationSearchService.constellationRelatedSearchResponses(keyword, userEntity));
    }

    @Operation(
            summary = "유저 검색 기능",
            description = "유저 검색 기능입니다. " +
                    "닉네임을 기준으로 게시물을 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/search/user")
    public Response<List<SearchResponse>> userSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(userSearchService.userSearch(keyword).stream().map(User::fromEntity).map(SearchResponse::fromUser).toList());
    }

    @Operation(
            summary = "유저 연관 검색 기능",
            description = "유저 연관 검색 기능입니다. " +
                    "닉네임을 기준으로 게시물을 5개 찾습니다. 최신순 정렬합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공", content = @Content(schema = @Schema(implementation = ArticleDetailResponse.class)))
            }
    )
    @GetMapping("/related-search/user")
    public Response<List<SearchResponse>> userRelatedSearch(@RequestParam String keyword) {
        log.info("request 정보 : {}", keyword);
        return Response.success(userSearchService.userRelatedSearch(keyword).stream().map(User::fromEntity).map(SearchResponse::fromUser).toList());
    }
}
