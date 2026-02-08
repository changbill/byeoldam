package com.ssafy.star.constellation.api;

import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.common.response.Response;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.constellation.dto.request.ConstellationCreateRequest;
import com.ssafy.star.constellation.dto.request.ConstellationModifyRequest;
import com.ssafy.star.constellation.dto.response.ConstellationForUserResponse;
import com.ssafy.star.constellation.dto.response.ConstellationResponse;
import com.ssafy.star.constellation.dto.response.ConstellationWithArticleResponse;
import com.ssafy.star.contour.dto.Contour;
import com.ssafy.star.user.application.FollowService;
import com.ssafy.star.user.dto.request.UserRequest;
import com.ssafy.star.user.dto.response.LikeUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConstellationController {
    private final ConstellationService constellationService;
    private final ArticleService articleService;
    private final FollowService followService;

    @Operation(
            summary = "별자리 생성",
            description = "별자리 생성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PostMapping("/constellations")
    public Response<Void> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart("request") ConstellationCreateRequest request,
            @RequestPart("origin") MultipartFile origin,
            @RequestPart("thumb") MultipartFile thumb,
            @RequestPart("cthumb") MultipartFile cthumb,
            @RequestPart("contoursList") List<List<List<Integer>>> contoursList,
            @RequestPart("ultimate") List<List<Integer>> ultimate
    ) throws IOException {
        log.debug("contoursList : {} contoursList : {}", contoursList, ultimate);
        // 사용자를 관리자로 만듦
        constellationService.create(
                userDetails.getUsername(),
                request.name(),
                origin,
                thumb,
                cthumb,
                contoursList,
                ultimate
        );
        return Response.success();
    }

    @Operation(
            summary = "별자리 수정",
            description = "별자리 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PutMapping("/constellations/{constellationId}")
    public Response<Void> modify(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long constellationId,
            @RequestPart("request") ConstellationModifyRequest request,
            @RequestPart("origin") MultipartFile origin,
            @RequestPart("thumb") MultipartFile thumb,
            @RequestPart("cthumb") MultipartFile cthumb,
            @RequestPart("contoursList") List<List<List<Integer>>> contoursList,
            @RequestPart("ultimate") List<List<Integer>> ultimate
    ) throws IOException {
        constellationService.modify(
                userDetails.getUsername(),
                constellationId,
                request.name(),
                origin,
                thumb,
                cthumb,
                contoursList,
                ultimate
        );
        return Response.success();
    }

    @Operation(
            summary = "별자리 삭제",
            description = "별자리 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @DeleteMapping("/constellations/{constellationId}")
    public Response<Void> deleteConstellationWithContour(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId) {
        constellationService.deleteConstellationWithContour(userDetails.getUsername(), constellationId);
        return Response.success();
    }

    @Operation(
            summary = "나의 우주 보기 - 별자리 전체 조회",
            description = "별자리 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @GetMapping("/constellations")
    public Response<List<ConstellationWithArticleResponse>> myConstellations(@AuthenticationPrincipal UserDetails userDetails) {
        return Response.success(constellationService.myConstellations(userDetails.getUsername()).stream().map(ConstellationWithArticleResponse::fromConstellationWithArticle).toList());
    }

    @Operation(
            summary = "유저의 별자리 전체 조회",
            description = "유저의 별자리 전체 조회입니다."
    )
    @GetMapping("/constellations/user/{nickname}")
    public Response<List<ConstellationWithArticleResponse>> userConstellations(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String nickname) {
        return Response.success(
                constellationService.userConstellations(nickname, userDetails.getUsername())
                        .stream()
                        .map(ConstellationWithArticleResponse::fromConstellationWithArticle)
                        .toList()
        );
    }

    @Operation(
            summary = "별자리 윤곽선 수정 요청을 보내면 현재 윤곽선 정보를 반환",
            description = "관련 이미지 3장, 윤곽선 리스트, 현재 선택되어 있는 윤곽선 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PostMapping("/constellations/{constellationId}/request-contour")
    public Response<Contour> requestModifyConstellation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId) {
        return Response.success(constellationService.requestModifyConstellation(userDetails.getUsername(), constellationId));
    }

    // TODO : 별자리 공유 신청, 수락 로직으로 바꿀 것
    @Operation(
            summary = "공유 별자리에 유저 추가",
            description = "공유 별자리에 유저를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PostMapping("/add-user/constellations/{constellationId}")
    public Response<Void> addUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId, @RequestBody UserRequest userRequest) {
        String nickname = userRequest.nickname();
        constellationService.addUser(constellationId, nickname, userDetails.getUsername());
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 삭제",
            description = "공유 별자리의 유저를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @DeleteMapping("/delete-user/constellations/{constellationId}")
    public Response<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId, @RequestBody UserRequest userRequest) {
        String nickname = userRequest.nickname();
        constellationService.deleteUser(constellationId, nickname, userDetails.getUsername());
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 조회",
            description = "공유 별자리의 유저를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @GetMapping("/users/constellations/{constellationId}")
    public Response<List<ConstellationForUserResponse>> userCheck(@PathVariable Long constellationId, @AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        return Response.success(constellationService.findConstellationUsers(constellationId));
    }

    @Operation(
            summary = "공유 별자리의 관리자 변경",
            description = "공유 별자리의 관리자를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ConstellationResponse.class)))
            }
    )
    @PutMapping("/role-modify/constellations/{constellationId}")
    public Response<Void> roleModify(@PathVariable Long constellationId, @RequestBody UserRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        String nickname = request.nickname();
        constellationService.roleModify(constellationId, nickname, userDetails.getUsername());
        return Response.success();
    }

    @Operation(
            summary = "별자리 좋아요 요청",
            description = "별자리 좋아요를 요청합니다."
    )
    @PostMapping("/constellations/{constellationId}/likes")
    public Response<Void> like(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId) {
        constellationService.like(constellationId, userDetails.getUsername());
        return Response.success();
    }

    @Operation(
            summary = "별자리 좋아요 상태 확인",
            description = "별자리 좋아요 상태를 확인합니다."
    )
    @GetMapping("/constellations/{constellationId}/likes")
    public Response<Boolean> checkLike(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long constellationId) {
        return Response.success(constellationService.checkLike(constellationId, userDetails.getUsername()));
    }

    @Operation(
            summary = "별자리 좋아요 갯수 확인",
            description = "별자리 좋아요의 개수를 확인합니다."
    )
    @GetMapping("/constellations/{constellationId}/likeCount")
    public Response<Integer> likeCount(@PathVariable Long constellationId) {
        return Response.success(constellationService.likeCount(constellationId));
    }

    @Operation(
            summary = "별자리를 좋아요한 사람의 목록 확인",
            description = "별자리를 좋아요한 사람의 목록을 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = LikeUserResponse.class)))
            }

    )
    @GetMapping("/constellations/{constellationId}/likeList")
    public Response<List<LikeUserResponse>> likeList(@PathVariable Long constellationId) {
        return Response.success(constellationService.likeList(constellationId).stream().map(LikeUserResponse::fromUser).toList());
    }
}
