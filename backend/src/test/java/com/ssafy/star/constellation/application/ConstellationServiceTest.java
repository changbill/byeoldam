package com.ssafy.star.constellation.application;

import com.ssafy.star.constellation.dao.ConstellationRepository;
import com.ssafy.star.constellation.dao.ConstellationUserRepository;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.constellation.domain.ConstellationUserEntity;
import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.repository.ContourRepository;
import com.ssafy.star.global.oauth.domain.ProviderType;
import com.ssafy.star.support.TestContainerSupport;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.ssafy.star.constellation.ConstellationUserRole.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConstellationServiceTest extends TestContainerSupport {

    private final ConstellationService constellationService;
    private final UserRepository userRepository;
    private final ConstellationRepository constellationRepository;
    private final ConstellationUserRepository constellationUserRepository;
    private final ContourRepository contourRepository;

    @Autowired
    ConstellationServiceTest(
            ConstellationService constellationService,
            UserRepository userRepository,
            ConstellationRepository constellationRepository,
            ConstellationUserRepository constellationUserRepository,
            ContourRepository contourRepository
    ) {
        this.constellationService = constellationService;
        this.userRepository = userRepository;
        this.constellationRepository = constellationRepository;
        this.constellationUserRepository = constellationUserRepository;
        this.contourRepository = contourRepository;
    }

    @Test
    void myConstellations_정상조회() {
        // given
        String email = uniqueEmail();
        UserEntity me = userRepository.save(testUser(email, uniqueNickname()));

        ContourEntity contour = contourRepository.save(
                ContourEntity.of("originUrl", "thumbUrl", "cThumbUrl", List.of(), List.of())
        );

        ConstellationEntity constellation = ConstellationEntity.of("ORION");
        constellation.setContourId(contour.get_id());
        constellationRepository.saveAndFlush(constellation);

        constellationUserRepository.saveAndFlush(ConstellationUserEntity.of(constellation, me, ADMIN));

        // when
        var result = constellationService.myConstellations(email, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0).name()).isEqualTo("ORION");
    }

    @Test
    void create_정상생성_S3는Fake로대체() throws Exception {
        // given
        String email = uniqueEmail();
        userRepository.save(testUser(email, uniqueNickname()));

        MockMultipartFile origin = new MockMultipartFile(
                "origin", "origin.png", "image/png", "o".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile thumb = new MockMultipartFile(
                "thumb", "thumb.png", "image/png", "t".getBytes(StandardCharsets.UTF_8)
        );
        MockMultipartFile cthumb = new MockMultipartFile(
                "cthumb", "cthumb.png", "image/png", "c".getBytes(StandardCharsets.UTF_8)
        );

        // when
        constellationService.create(
                email,
                "ORION",
                origin, thumb, cthumb,
                List.of(List.of(List.of(1, 2))),
                List.of(List.of(3, 4))
        );

        // then
        var result = constellationService.myConstellations(email, PageRequest.of(0, 10));
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("ORION");
        ConstellationEntity saved = constellationRepository.findById(result.getContent().get(0).id()).orElseThrow();
        assertThat(saved.getContourId()).isNotNull();

        assertThat(contourRepository.findById(saved.getContourId())).isPresent();
        assertThat(constellationUserRepository.findByConstellationEntity(saved, PageRequest.of(0, 10)).getContent()).hasSize(1);
    }

    // ----------------- helpers -----------------

    private static UserEntity testUser(String email, String nickname) {
        // UserEntity는 email/providerType/roleType 등이 setter가 없어서 of() 사용이 정석입니다.
        ProviderType providerType = ProviderType.values()[0]; // enum 상수명을 몰라도 안전하게 하나 선택
        return UserEntity.of(email, providerType, "test-password", "테스트이름", nickname);
    }

    private static String uniqueEmail() {
        return "test-" + UUID.randomUUID() + "@example.com";
    }

    private static String uniqueNickname() {
        return "nick" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

}
