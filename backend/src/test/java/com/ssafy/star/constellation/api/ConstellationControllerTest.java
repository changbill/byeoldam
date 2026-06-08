package com.ssafy.star.constellation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.user.application.FollowService;
import com.ssafy.star.user.dto.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConstellationController.class)
@AutoConfigureMockMvc(addFilters = false) // 컨트롤러 매핑만 볼 거라 Security 필터는 끔
class ConstellationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ConstellationService constellationService;
    @MockBean
    ArticleService articleService;
    @MockBean
    FollowService followService;

    @NonNull
    private MockMultipartFile getRequestPart() {
        return new MockMultipartFile(
                "request",
                "request.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                        {"name":"ORION"}
                        """.getBytes(StandardCharsets.UTF_8)
        );
    }

    @NonNull
    private MockMultipartFile getThumb() {
        return new MockMultipartFile("thumb", "thumb.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
    }

    @NonNull
    private MockMultipartFile getCthumb() {
        return new MockMultipartFile("cthumb", "cthumb.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
    }

    @NonNull
    private MockMultipartFile getOrigin() {
        return new MockMultipartFile("origin", "origin.png", MediaType.IMAGE_PNG_VALUE, "x".getBytes());
    }

    @NonNull
    private MockMultipartFile getContoursList() {
        return new MockMultipartFile(
                "contoursList",
                "contoursList.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                        [[[1,2],[3,4]],[[5,6],[7,8]]]
                        """.getBytes(StandardCharsets.UTF_8)
        );
    }

    @NonNull
    private MockMultipartFile getUltimate() {
        return new MockMultipartFile(
                "ultimate",
                "ultimate.json",
                MediaType.APPLICATION_JSON_VALUE,
                """
                        [[10,20],[30,40]]
                        """.getBytes(StandardCharsets.UTF_8)
        );
    }

    @NonNull
    private UserRequest getUserRequest() {
        return new UserRequest(
                "userNickname"
        );
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final long CONSTELLATION_ID = 1L;

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
        ).andExpect(status().isOk());

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
        when(constellationService.likeList(eq(constellationId), any(Pageable.class))).thenReturn(Page.empty());
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
        ).andExpect(status().isOk());

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
        ).andExpect(status().isOk());

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
        ).andExpect(status().isOk());

        verify(constellationService).checkLike(constellationId, "test-user@example.com");
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 내_별자리_전체조회() throws Exception {
        // given
        when(constellationService.myConstellations(eq("test-user@example.com"), any(Pageable.class)))
                .thenReturn(Page.empty());

        // when&then
        mockMvc.perform(
                get("/api/v1/constellations")
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).myConstellations(eq("test-user@example.com"), any(Pageable.class));
        verifyNoMoreInteractions(constellationService);
    }

    // todo: Security 권한 설정 추가예정
    @Test
    @WithMockUser(username = "test-user@example.com")
    void 유저_별자리_전체조회() {
        // given
        // when&then
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_윤곽선_정보_반환() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
        mockMvc.perform(
                post("/api/v1/constellations/{constellationId}/request-contour", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).requestModifyConstellation("test-user@example.com", constellationId);
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 공유_별자리에_유저추가() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;
        UserRequest userRequest = getUserRequest();
        String json = objectMapper.writeValueAsString(userRequest);

        // when&then
        mockMvc.perform(
                post("/api/v1/constellations/add-user/{constellationId}", constellationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).addUser(
                CONSTELLATION_ID,
                "userNickname",
                "test-user@example.com");
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 공유별자리_유저_삭제() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;
        UserRequest userRequest = getUserRequest();
        String json = objectMapper.writeValueAsString(userRequest);

        // when&then
        mockMvc.perform(
                delete("/api/v1/constellations/delete-user/{constellationId}", constellationId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(constellationService).deleteUser(
                constellationId,
                userRequest.nickname(),
                "test-user@example.com"
        );
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 공유별자리의_유저_조회() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
        mockMvc.perform(
                get("/api/v1/constellations/users/{constellationId}", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).findConstellationUsers(eq(constellationId), any(Pageable.class));
        verifyNoMoreInteractions(constellationService);
    }

    // todo: security 권한 추가
    @Test
    @WithMockUser(username = "test-user@example.com")
    void roleModify() {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_좋아요_요청() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
        mockMvc.perform(
                post("/api/v1/constellations/{constellationId}/likes", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).like(constellationId, "test-user@example.com");
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_좋아요_중인지_확인() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
        mockMvc.perform(
                get("/api/v1/constellations/{constellationId}/likes", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).checkLike(constellationId, "test-user@example.com");
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    @WithMockUser(username = "test-user@example.com")
    void 별자리_좋아요_개수_확인() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;

        // when&then
        mockMvc.perform(
                get("/api/v1/constellations/{constellationId}/likeCount", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).likeCount(constellationId);
        verifyNoMoreInteractions(constellationService);
    }

    @Test
    void 별자리_좋아요_목록_확인() throws Exception {
        // given
        long constellationId = CONSTELLATION_ID;
        when(constellationService.likeList(eq(constellationId), any(Pageable.class))).thenReturn(Page.empty());

        // when&then
        mockMvc.perform(
                get("/api/v1/constellations/{constellationId}/likelist", constellationId)
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(constellationService).likeList(eq(constellationId), any(Pageable.class));
        verifyNoMoreInteractions(constellationService);
    }
}
