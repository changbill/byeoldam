package com.ssafy.star.user.api;

import com.ssafy.star.common.response.Response;
import com.ssafy.star.user.application.FollowService;
import com.ssafy.star.user.application.UserService;
import com.ssafy.star.user.domain.ApprovalStatus;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.dto.request.*;
import com.ssafy.star.user.dto.response.UserJoinResponse;
import com.ssafy.star.user.dto.response.UserLoginResponse;
import com.ssafy.star.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User 컨트롤러", description = "로그인, 회원가입, 팔로잉에 관한 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    @Operation(
            summary = "이메일 인증코드 요청",
            description = "email의 정보를 받아 현재 DB에 저장되어 있지 않은 메일인지 체크 후 인증 코드를 해당 메일로 보냄"
    )
    @PostMapping("/email/verification-request")
    public Response<Void> sendMessage(@RequestBody EmailRequest request){
        userService.sendCodeByEmail(request.email());
        return Response.success();
    }

    @Operation(
            summary = "이메일 인증 코드 검증",
            description = "응답이 true일 경우 검증 성공, 그 외에는 에러를 반환"
    )
    @GetMapping("/email/verification")
    public Response<Boolean> verifyEmailCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        return Response.success(userService.verifyEmailCode(email, code));
    }

    @Operation(
            summary = "회원가입",
            description = "email, password, name, nickname의 정보를 받아 회원가입을 진행한다." +
                    "이후 회원 정보 수정에서 생일, 메모, 공개/비공개 여부를 설정할 수 있다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = UserJoinResponse.class)))
            }
    )
    @PostMapping("/users/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.email(), request.password(), request.name(), request.nickname());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @Operation(
            summary = "로그인",
            description = "유저의 email, password를 입력하여 로그인을 진행한다. 이후 JWT 토큰 발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 및 JWT 토큰 발급", content = @Content(schema = @Schema(implementation = UserLoginResponse.class)))
            }
    )
    @PostMapping("/users/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.email(), request.password());
        return Response.success(new UserLoginResponse(token));
    }

    @Operation(
            summary = "중복된 이메일 확인체크(True일 경우 중복에 해당하지 않음)",
            description = "이미 가입된 이메일인지 체크한다. True일 경우 가입한 적이 없고, False일 경우 동일한 이메일로 가입한 기록이 있다."
    )
    @PostMapping("/users/check-email")
    public Response<Boolean> checkDuplicateEmail(@RequestBody EmailRequest request) {
        return Response.success(userService.checkDuplicateEmail(request.email()));
    }

    @Operation(
            summary = "중복된 닉네임 확인 체크(참일 경우 중복 없음)",
            description = "이미 가입된 닉네임인지 체크한다. True일 경우 가입한 적이 없고, False일 경우 동일한 닉네임이 존재한다."
    )
    @PostMapping("/users/check-nickname")
    public Response<Boolean> checkDuplicateNickname(@RequestBody NicknameRequest request) {
        return Response.success(userService.checkDuplicateNickname(request.nickname()));
    }

    @Operation(
            summary = "프로필 보기",
            description = "프로필, 이름, 닉네임, 메모 등의 정보 등을 보여준다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 보기", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("/users/{nickname}")
    public Response<UserResponse> myProfile(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(UserResponse.fromUser(userService.my(nickname)));
    }

    @Operation(
            summary = "프로필 수정하기",
            description = "프로필, 비밀번호, 이름, 닉네임, 메모 계정 공개/비공개, 생일 등의 정보 등을 수정한다."
    )
    @PutMapping("/users")
    public Response<Void> updateMyProfile(Authentication authentication, @RequestBody UserModifyRequest request) {
        userService.updateMyProfile(authentication.getName(), request.password(), request.name(), request.nickname(), request.memo(), request.disclosureType(), request.birthday());
        return Response.success();
    }

    @Operation(
            summary = "프로필 계정 삭제하기"
    )
    @DeleteMapping("/users")
    public Response<Void> deleteUser(Authentication authentication) {
        userService.delete(authentication.getName());
        return Response.success();
    }

    @Operation(
            summary = "팔로우 요청하기",
            description = "팔로우 요청, 팔로우 신청, 팔로우 취소 등의 기능을 담당한다. " +
                    "프로필 공개 유저에게 팔로우를 신청할 경우 바로 팔로잉 할 수 있다." +
                    "프로필 비공개 유저에게 팔로우 신청할 경우 팔로우 요청 후 승인 단계로 거쳐야 한다." +
                    "이미 팔로우를 요청한 상태이거나 팔로잉 중일 때는 해당 API를 호출 시 팔로우 취소가 된다." +
                    "ACCEPT - 팔로우 승인, REQUEST - 팔로우 요청, CANCEL - 팔로우 취소",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 상태 보기", content = @Content(schema = @Schema(implementation = ApprovalStatus.class)))
            }
    )
    @PostMapping("/{nickname}/follow")
    public Response<ApprovalStatus> requestFollow(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.requestFollow(authentication.getName(), nickname));
    }

    @Operation(
            summary = "팔로우 상태 확인하기",
            description = "NOTHING - 현재 팔로잉 중이 아님" +
                    "ACCEPT - 현재 팔로잉 상태" +
                    "REQUEST - 팔로잉을 요청한 상태",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로우 상태 보기", content = @Content(schema = @Schema(implementation = ApprovalStatus.class)))
            }
    )
    @GetMapping("/{nickname}/follow")
    public Response<ApprovalStatus> followStatus(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.followStatus(authentication.getName(), nickname));
    }


    @Operation(
            summary = "나에게 팔로우를 신청한 사람들의 리스트 보기",
            description = "나의 프로필이 비공개인 경우 다른 사람이 팔로우를 신청했을 때 해당 리스트를 볼 수 있다. " +
                    "나의 프로필이 공개일 경우 팔로우 요청 시 바로 팔로잉 관계가 되기 때문에 해당 API는 의미가 없다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로우 신청한 사람들 정보", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("me/request-follow")
    public Response<List<UserResponse>> requestFollowList(Authentication authentication) {
        return Response.success(followService.requestFollowList(authentication.getName())
                .stream()
                .map(UserResponse::fromUser)
                .toList());
    }

    @Operation(
            summary = "팔로우 요청 승인하기",
            description = "유저가 비공개일 경우 해당 API를 통해 팔로우 승인 및 거절을 할 수 있다. 응답은 상대 유저의 닉네임과 함께 CANCEL or ACCEPT를 보낸다. 이외의 응답은 에러 처리된다. " +
                    "Response를 CANCEL : 팔로우 거절" +
                    "Response를 ACCEPT : 팔로우 승인",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CANCEL or ACCEPT 전달", content = @Content(schema = @Schema(implementation = ApprovalStatus.class)))
            }
    )
    @PostMapping("/me/approve-follow")
    public Response<ApprovalStatus> acceptFollow(Authentication authentication, @RequestBody FollowAcceptRequest request) {
        return Response.success(followService.acceptFollow(authentication.getName(), request.toUserNickname(), request.status()));
    }

    @Operation(
            summary = "내 팔로워 확인하기",
            description = "나의 팔로워 리스트를 확인한다. 로그인한 유저만 접근 가능하다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로워한 유저들의 정보 반환", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("/me/followers")
    public Response<List<UserResponse>> followers(Authentication authentication) {
        return Response.success(followService.followers(authentication.getName()).stream().map(UserResponse::fromUser).toList());
    }

    @Operation(
            summary = "내 팔로잉 확인하기",
            description = "나의 팔로잉 리스트를 확인한다. 로그인한 유저만 접근 가능하다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로잉한 유저들의 정보 반환", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("/me/followings")
    public Response<List<UserResponse>> followings(Authentication authentication) {
        return Response.success(followService.followings(authentication.getName()).stream().map(UserResponse::fromUser).toList());
    }

    @Operation(
            summary = "남의 팔로워 확인하기",
            description = "남의 팔로워 리스트를 확인한다. 해당 유저의 프로필이 공개이거나, 비공개 상태일 경우 팔로잉 상태일 경우만 볼 수 있다. 그 외의 경우 INVALID_PERMISSION 예외상황이 발생",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로워중인 유저들의 정보 반환", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("/{nickname}/followers")
    public Response<List<UserResponse>> otherFollowers(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.otherFollowers(authentication.getName(), nickname).stream().map(UserResponse::fromUser).toList());

    }

    @Operation(
            summary = "남의 팔로잉 확인하기",
            description = "남의 팔로잉 리스트를 확인한다. 해당 유저의 프로필이 공개이거나, 비공개 상태일 경우 팔로잉 상태일 경우만 볼 수 있다. 그 외의 경우 INVALID_PERMISSION 예외상황이 발생",
            responses = {
                    @ApiResponse(responseCode = "200", description = "팔로워중인 유저들의 정보 반환", content = @Content(schema = @Schema(implementation = UserResponse.class)))
            }
    )
    @GetMapping("/{nickname}/followings")
    public Response<List<UserResponse>> otherFollowings(Authentication authentication, @PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.otherFollowings(authentication.getName(), nickname).stream().map(UserResponse::fromUser).toList());
    }

    @Operation(
            summary = "팔로워 수 반환하기",
            description = "팔로워 수를 Long 타입으로 반환한다."
    )
    @GetMapping("/{nickname}/count-followers")
    public Response<Long> countFollowers(@PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.countFollowers(nickname));
    }

    @Operation(
            summary = "팔로잉 수 반환하기",
            description = "팔로잉 수를 Long 타입으로 반환한다."
    )
    @GetMapping("/{nickname}/count-followings")
    public Response<Long> countFollowings(@PathVariable(name = "nickname") String nickname) {
        return Response.success(followService.countFollowings(nickname));
    }
}
