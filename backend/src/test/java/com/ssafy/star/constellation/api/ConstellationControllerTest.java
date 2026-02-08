package com.ssafy.star.constellation.api;

import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.user.application.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConstellationController.class)
@AutoConfigureMockMvc(addFilters = false) // 컨트롤러 매핑만 볼 거라 Security 필터는 끔
class ConstellationControllerTest {

    @Autowired MockMvc mockMvc;

    @MockBean ConstellationService constellationService;
    @MockBean ArticleService articleService;
    @MockBean FollowService followService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // 👈 이 설정이 반드시 있어야 Authentication 파라미터가 주입됩니다!
                .build();
    }

    @NonNull
    private static MockMultipartFile getRequestPart() {
        MockMultipartFile requestPart = new MockMultipartFile(
                "request",
                "request.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                {"name":"ORION"}
                """.getBytes(StandardCharsets.UTF_8)
        );
        return requestPart;
    }

    @NonNull
    private static MockMultipartFile getThumb() {
        MockMultipartFile thumb = new MockMultipartFile("thumb", "thumb.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
        return thumb;
    }

    @NonNull
    private static MockMultipartFile getCthumb() {
        MockMultipartFile cthumb = new MockMultipartFile("cthumb", "cthumb.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
        return cthumb;
    }

    @NonNull
    private static MockMultipartFile getOrigin() {
        MockMultipartFile origin = new MockMultipartFile("origin", "origin.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
        return origin;
    }

    @NonNull
    private static MockMultipartFile getContoursList() {
        MockMultipartFile contoursList = new MockMultipartFile(
                "contoursList",
                "contoursList.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                [[[1,2],[3,4]],[[5,6],[7,8]]]
                """.getBytes(StandardCharsets.UTF_8)
        );
        return contoursList;
    }

    @NonNull
    private static MockMultipartFile getUltimate() {
        MockMultipartFile ultimate = new MockMultipartFile(
                "ultimate",
                "ultimate.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                [[10,20],[30,40]]
                """.getBytes(StandardCharsets.UTF_8)
        );
        return ultimate;
    }

    private static long CONSTELLATION_ID = 1L;

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_생성() throws Exception {
        // given
        MockMultipartFile requestPart = getRequestPart();
        MockMultipartFile origin = getOrigin();
        MockMultipartFile thumb = getThumb();
        MockMultipartFile cthumb = getCthumb();
        MockMultipartFile contoursList = getContoursList();
        MockMultipartFile ultimate = getUltimate();

        // when & then
        mockMvc.perform(
                        multipart("/api/v1/constellations")
                                .file(requestPart)
                                .file(origin)
                                .file(thumb)
                                .file(cthumb)
                                .file(contoursList)
                                .file(ultimate)
                                .with(csrf())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk());

        verify(constellationService).create(
                eq("test-user@example.com"),
                eq("ORION"),
                any(), any(), any(),
                anyList(),
                anyList()
        );
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_수정() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;
        MockMultipartFile requestPart = getRequestPart();
        MockMultipartFile origin = getOrigin();
        MockMultipartFile thumb = getThumb();
        MockMultipartFile cthumb = getCthumb();
        MockMultipartFile contoursList = getContoursList();
        MockMultipartFile ultimate = getUltimate();

        // when & then
        mockMvc.perform(
                        multipart("/api/v1/constellations/{constellationId}", constellationId)
                                .file(requestPart)
                                .file(origin)
                                .file(thumb)
                                .file(cthumb)
                                .file(contoursList)
                                .file(ultimate)
                                .with(req -> {
                                    req.setMethod("PUT");
                                    return req;
                                })
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(constellationService).modify(
                eq("test-user@example.com"),
                eq(constellationId),
                eq("ORION"),
                any(), any(), any(),
                anyList(),
                anyList()
        );
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_삭제() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when & then
        mockMvc.perform(
                        delete("/api/v1/constellations/{constellationId}", constellationId)
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(constellationService).deleteConstellationWithContour("test-user@example.com", constellationId);
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_좋아요_확인() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when & then
        mockMvc.perform(
                        get("/api/v1/constellations/{constellationId}/likes", constellationId)
                                .with(csrf())
                )
                .andExpect(status().isOk());

        verify(constellationService).checkLike(constellationId, "test-user@example.com");
        verifyNoMoreInteractions(constellationService);
    }
}