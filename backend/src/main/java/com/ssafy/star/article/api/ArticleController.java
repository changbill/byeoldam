package com.ssafy.star.article.api;


import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.request.ArticleConstellationSelect;
import com.ssafy.star.article.dto.request.ArticleCreateRequest;
import com.ssafy.star.article.dto.request.ArticleDeletionUndo;
import com.ssafy.star.article.dto.request.ArticleModifyRequest;
import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.article.dto.response.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.ssafy.star.comment.application.CommentService;
import com.ssafy.star.comment.dto.response.CommentResponse;
import com.ssafy.star.user.domain.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    //TODO : 주소에서 articles 위치 변경

    @Operation(
            summary = "게시물 작성",
            description = "게시물 작성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/articles")
    public Response<Void> create(@RequestBody ArticleCreateRequest request, Authentication authentication) {
        // TODO : image
        articleService.create(request.title(), request.tag(), request.description(),
                request.disclosureType(), authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "게시물 수정",
            description = "게시물 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/articles/{articleId}")
    public Response<ArticleResponse> modify(@PathVariable Long articleId, @RequestBody ArticleModifyRequest request, Authentication authentication) {
        Article article = articleService.modify(articleId, request.title(), request.tag(), request.description(),
                request.disclosureType(), authentication.getName());
        return Response.success(ArticleResponse.fromArticle(article));
    }

    @Operation(
            summary = "게시물 삭제",
            description = "게시물 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @DeleteMapping("/articles/{articleId}")
    public Response<Void> delete(@PathVariable Long articleId, Authentication authentication) {
        articleService.delete(articleId, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "게시물 전체 조회",
            description = "게시물 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles")
    public Response<Page<ArticleResponse>> list(Pageable pageable, Authentication authentication) {
        String email = authentication.getName();
        return Response.success(articleService.list(email, pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "유저의 게시물 전체 조회",
            description = "유저의 게시물 전체 조회입니다. 유저가 접속자일 경우 전체 조회, "+
                          "접속자가 유저를 팔로우 중일 경우 전체 조회, "+
                          "그 외 discloseType이 VISIBLE인 게시물만 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/user/{userEmail}")
    public Response<Page<ArticleResponse>> userArticlePage(@PathVariable String userEmail, Authentication authentication, Pageable pageable) {
        return Response.success(articleService.userArticlePage(userEmail, authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "게시물 상세 조회",
            description = "게시물 상세 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/{articleId}")
    public Response<ArticleResponse> read(@PathVariable Long articleId, Authentication authentication, Pageable pageable) {
        String email = authentication.getName();

        return Response.success(ArticleResponse.fromArticle(articleService.detail(articleId, email)));
    }

    @Operation(
            summary = "별자리 배정 및 변경",
            description = "게시물에 별자리를 배정하거나 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "배정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/constellation-select/articles/{articleId}")
    public Response<Void> select(@PathVariable Long articleId, @RequestBody ArticleConstellationSelect articleConstellationSelect, Authentication authentication) {
        articleService.select(articleId, articleConstellationSelect.constellationId(), authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "별자리에 있는 게시물 전체 조회",
            description = "별자리에 있는 게시물 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/constellation/{constellationId}")
    public Response<Page<ArticleResponse>> articlesInConstellation(@PathVariable Long constellationId, Authentication authentication, Pageable pageable) {
        return Response.success(articleService.articlesInConstellation(constellationId, authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "휴지통 조회",
            description = "휴지통을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/articles/trashcan")
    public Response<Page<ArticleResponse>> trashcan(Authentication authentication, Pageable pageable) {
        return Response.success(articleService.trashcan(authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @Operation(
            summary = "게시물 복원",
            description = "휴지통에 있던 게시물을 복원합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "복원 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/articles/trashcan/undo")
    public Response<ArticleResponse> undoDeletion(@RequestBody ArticleDeletionUndo articleDeletionUndo, Authentication authentication) {
        return Response.success(ArticleResponse.fromArticle(articleService.undoDeletion(articleDeletionUndo.articleId(),authentication.getName())));
    }

    // TODO : 팔로우 피드
    //
}
