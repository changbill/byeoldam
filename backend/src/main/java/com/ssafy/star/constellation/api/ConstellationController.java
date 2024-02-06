package com.ssafy.star.constellation.api;

import com.ssafy.star.article.dto.response.ArticleResponse;
import com.ssafy.star.common.response.Response;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.constellation.dto.request.ConstellationCreateRequest;
import com.ssafy.star.constellation.dto.request.ConstellationModifyRequest;
import com.ssafy.star.constellation.dto.request.UserEmailRequest;
import com.ssafy.star.constellation.dto.response.ConstellationResponse;
import com.ssafy.star.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class ConstellationController {

    private final ConstellationService constellationService;



    @Operation(
            summary = "별자리 생성",
            description = "별자리 생성입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/constellations")
    public Response<Void> create(@RequestBody ConstellationCreateRequest request, Authentication authentication) {
        String email = authentication.getName();
        //TODO : 윤곽선

        // 사용자를 관리자로 만듦
        constellationService.create(
                request.name(),
                request.shared(),
                request.description(),
                authentication.getName()
        );
        return Response.success();
    }

    @Operation(
            summary = "별자리 수정",
            description = "별자리 수정입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/constellations/{constellationId}")
    public Response<ConstellationResponse> modify(@PathVariable Long constellationId, @RequestBody ConstellationModifyRequest request, Authentication authentication) {

        Constellation constellation = constellationService.modify(
                constellationId,
                request.name(),
                request.shared(),
                request.description(),
                authentication.getName()
        );
        return Response.success(ConstellationResponse.fromConstellation(constellation));
    }

    @Operation(
            summary = "별자리 삭제",
            description = "별자리 삭제입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @DeleteMapping("/constellations/{constellationId}")
    public Response<Void> delete(@PathVariable Long constellationId, Authentication authentication) {
        constellationService.delete(constellationId, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "별자리 전체 조회",
            description = "별자리 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/constellations")
    public Response<Page<ConstellationResponse>> list(Pageable pageable, Authentication authentication) {
        return Response.success(constellationService.list(authentication.getName(), pageable).map(ConstellationResponse::fromConstellation));
    }

    @Operation(
            summary = "유저의 별자리 전체 조회",
            description = "유저의 별자리 전체 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/constellations/user/{email}")
    public Response<Page<ConstellationResponse>> userConstellations(@PathVariable String email, Authentication authentication, Pageable pageable) {
        return Response.success(constellationService.userConstellations(email,authentication.getName(),pageable).map(ConstellationResponse::fromConstellation));
    }

    @Operation(
            summary = "별자리 상세 조회",
            description = "별자리 상세 조회입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/constellations/{constellationId}")
    public Response<ConstellationResponse> read(@PathVariable Long constellationId, Authentication authentication) {
        return Response.success(ConstellationResponse.fromConstellation(constellationService.detail(constellationId, authentication.getName())));
    }

    // TODO : 별자리 공유 신청, 수락 로직으로 바꿀 것
    @Operation(
            summary = "공유 별자리에 유저 추가",
            description = "공유 별자리에 유저를 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PostMapping("/add-user/constellations/{constellationId}")
    public Response<Void> addUser(@PathVariable Long constellationId, @RequestBody UserEmailRequest userEmailRequest, Authentication authentication) {
        String userEmail = userEmailRequest.userEmail();
        constellationService.addUser(constellationId, userEmail, authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 삭제",
            description = "공유 별자리의 유저를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @DeleteMapping("/delete-user/constellations/{constellationId}")
    public Response<Void> deleteUser(@PathVariable Long constellationId, @RequestBody UserEmailRequest userEmailRequest, Authentication authentication, Pageable pageable) {
        String userEmail = userEmailRequest.userEmail();
        constellationService.deleteUser(constellationId, userEmail, authentication.getName(), pageable);
        return Response.success();
    }

    @Operation(
            summary = "공유 별자리의 유저 조회",
            description = "공유 별자리의 유저를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @GetMapping("/users/constellations/{constellationId}")
    public Response<Page<UserResponse>> userCheck(@PathVariable Long constellationId, Authentication authentication, Pageable pageable) {
        return Response.success(constellationService.findSharedUsers(constellationId, authentication.getName(), pageable).map(UserResponse::fromUser));
    }

    @Operation(
            summary = "공유 별자리의 관리자 변경",
            description = "공유 별자리의 관리자를 변경합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = ArticleResponse.class)))
            }
    )
    @PutMapping("/role-modify/constellations/{constellationId}")
    public Response<Void> roleModify(@PathVariable Long constellationId, @RequestBody UserEmailRequest userEmailRequest, Authentication authentication) {
        String userEmail = userEmailRequest.userEmail();
        constellationService.roleModify(constellationId, userEmail, authentication.getName());
        return Response.success();
    }
}
