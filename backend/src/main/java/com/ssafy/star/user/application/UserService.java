package com.ssafy.star.user.application;

import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.common.types.DisclosureType;
import com.ssafy.star.global.auth.util.JwtTokenUtils;
import com.ssafy.star.global.email.Repository.EmailCacheRepository;
import com.ssafy.star.global.email.application.EmailService;
import com.ssafy.star.user.domain.FollowEntity;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.dto.User;
import com.ssafy.star.user.repository.FollowRepository;
import com.ssafy.star.user.repository.UserCacheRepository;
import com.ssafy.star.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final EmailCacheRepository emailCacheRepository;
    private final FollowRepository followRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Value("${mail.auth-code-expired-ms}")
    private Long mailExpiredMs;

    public Optional<User> loadUserByEmail(String email) {
        return Optional.ofNullable(userCacheRepository.getUser(email)
                .orElseGet(() ->
                        userRepository.findByEmail(email)
                                .map(User::fromEntity)
                                .orElseThrow(() ->
                                        new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
                                )
                )
        );
    }

    // 이메일 인증 요청 시도
    @Transactional
    public void sendCodeByEmail(String email) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        String title = "[별을담다] 안녕하세요. 이메일 인증 코드입니다.";
        String authCode = generateMailCode();  // 코드 생성
        String content = "[별을 담다]서비스에 방문해주셔서 감사합니다." +
                "\n\n" +
                "인증번호는 " + authCode +
                "입니다." +
                "\n" +
                "5분안에 입력해주세요." +
                "\n\n" +
                "감사합니다." +
                "\n\n" +
                "- 별을 담다 서비스팀 -";
        emailService.sendEmail(email, title, content);  // 메일 보내기
        emailCacheRepository.setEmailCode(email, authCode, expiredTimeMs);  // 레디스에 저장
    }

    // 이메일 인증코드 검증
    public boolean verifyEmailCode(String email, String userAuthCode) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        String authCode = emailCacheRepository.getEmailCode(email).orElseThrow(() -> new ByeolDamException(ErrorCode.NEVER_ATTEMPT_EMAIL_AUTH));

        if (!userAuthCode.equals(authCode)) {
            throw new ByeolDamException(ErrorCode.INVALID_EMAIL_CODE);
        }
        return true;
    }


    // 이메일 중복 체크(참일 경우 중복되는 이메일 없음)
    public boolean checkDuplicateEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    // 닉네임 중복 체크(참일 경우 중복되는 닉네임 없음)
    public boolean checkDuplicateNickname(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    // 로그인
    public String login(String email, String password) {
        // 회원가입 여부 체크
        User user = loadUserByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))
        );
        userCacheRepository.setUser(user);

        // 비밀번호 체크
        if (!encoder.matches(password, user.password())) {
            throw new ByeolDamException(ErrorCode.INVALID_PASSWORD);
        }
        // 토큰 생성 후 리턴
        String token = JwtTokenUtils.generateToken(email, user.nickname(), secretKey, expiredTimeMs);
        return token;
    }

    // 회원가입
    @Transactional
    public User join(String email, String password, String name, String nickname) {
        validateEmailPattern(email);
        checkEmailExistenceOrException(email);
        if (satisfyNickname(nickname)) {
            UserEntity userEntity = UserEntity.of(email, encoder.encode(password), name, nickname, null, DisclosureType.VISIBLE, null);
            return User.fromEntity(userRepository.save(userEntity));
        }
        throw new ByeolDamException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    //회원정보 조회
    @Transactional(readOnly = true)
    public User my(String nickName) {
        UserEntity userEntity = userRepository.findByNickname(nickName).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", nickName)));
        return User.fromEntity(userEntity);
    }

    //회원정보 수정
    @Transactional
    public void updateMyProfile(
            String email,
            String password,
            String name,
            String nickname,
            String memo,
            DisclosureType disclosureType,
            LocalDate birthday) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        //닉네임 중복체크
        if (satisfyNickname(nickname)) {
            userEntity.setNickname(nickname);
        }
        if (password != null) {
            userEntity.setPassword(encoder.encode(password));
        }
        if (name != null) {
            userEntity.setName(name);
        }
        if (birthday != null) {
            userEntity.setBirthday(birthday);
        }
        if (disclosureType != null) {
            userEntity.setDisclosureType(disclosureType);
        }
        //null이어도 되는 필드
        userEntity.setMemo(memo);

        userRepository.saveAndFlush(userEntity);
    }

    //회원 탈퇴
    @Transactional
    public void delete(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", email)));
        List<FollowEntity> my = followRepository.findByFromUserOrToUser(userEntity, userEntity);
        followRepository.deleteAllInBatch(my);
        userRepository.delete(userEntity);
    }

    //이메일 인증코드 생성
    private String generateMailCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("이메일 인증 코드 생성 중 에러 발생 : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean satisfyNickname(String nickname) {
        validateNicknamePattern(nickname);
        userRepository.findByNickname(nickname).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_NICKNAME, String.format("%s is duplcated", nickname));
                }
        );
        return true;
    }

    private void validateEmailPattern(String email) {
        final String REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern regex = Pattern.compile(REGEX);
        Matcher matcher = regex.matcher(email);

        if(!matcher.matches()) {
            throw new ByeolDamException(ErrorCode.UNSUITABLE_EMAIL, String.format("%s doesn't meet the conditions", email));
        }
    }

    private void validateNicknamePattern(String nickname) {
        //오직 소문자, 숫자, 4자 이상의 패턴만 가능
        final String REGEX = "^(?=.*[a-z])[a-z0-9_]{4,}$";
        Pattern regex = Pattern.compile(REGEX);
        Matcher matcher = regex.matcher(nickname);

        if (!matcher.matches()) {
            throw new ByeolDamException(ErrorCode.UNSUITABLE_NICKNAME, String.format("%s doesn't meet the conditions", nickname));
        }
    }

    // 해당 이메일이 이미 존재하는 이메일인지 확인 체크
    private void checkEmailExistenceOrException(String email) {
        userRepository.findByEmail(email).ifPresent(it -> {
                    throw new ByeolDamException(ErrorCode.DUPLICATED_USER_EMAIL, String.format("%s is duplicated", email));
                }
        );
    }

}
