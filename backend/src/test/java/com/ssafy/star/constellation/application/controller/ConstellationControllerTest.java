package com.ssafy.star.constellation.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.star.ByeolDamApplication;
import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.application.ArticleService;
import com.ssafy.star.article.application.fixture.ArticleEntityFixture;
import com.ssafy.star.article.dto.Article;
import com.ssafy.star.article.dto.request.ArticleCreateRequest;
import com.ssafy.star.article.dto.request.ArticleModifyRequest;
import com.ssafy.star.common.exception.ByeolDamException;
import com.ssafy.star.common.exception.ErrorCode;
import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.constellation.application.ConstellationService;
import com.ssafy.star.constellation.application.fixture.ConstellationEntityFixture;
import com.ssafy.star.constellation.dto.Constellation;
import com.ssafy.star.constellation.dto.request.ConstellationCreateRequest;
import com.ssafy.star.constellation.dto.request.ConstellationModifyRequest;
import com.ssafy.star.user.domain.UserEntity;
import com.ssafy.star.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

import static com.ssafy.star.article.DisclosureType.VISIBLE;
import static com.ssafy.star.constellation.SharedType.SHARED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = ByeolDamApplication.class)
@AutoConfigureMockMvc
public class ConstellationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; // 직렬화 역직렬화
    @MockBean
    private ConstellationService constService;
    @MockBean
    UserRepository userRepository;

    @Test
    @WithMockUser
    void 별자리등록() throws Exception {
        String name = "name";
        SharedType shared = SHARED;
        String description = "설명입니다";

        mockMvc.perform(post("/api/v1/constellations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new ConstellationCreateRequest(name, shared, description)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser
//    void 별자리수정() throws Exception {
//
//        String name = "name";
//        SharedType shared = SHARED;
//        String description = "설명입니다";
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        UserEntity userEntity = getUserEntityOrException(email);   // 현재 사용자
//
//        when(constService.modify(any(), eq(name), eq(shared), eq(description), eq(email)))
//                .thenReturn(Constellation.fromEntity(ConstellationEntityFixture.get(
//                        name,
//                        shared,
//                        description,
//                        userEntity
//                )));
//
//        mockMvc.perform(put("/api/v1/constellations/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new ConstellationModifyRequest(
//                                name,
//                                shared,
//                                description
//                        )))
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockUser
//    void 별자리수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception {
//        // 필요한 것
//        // 수정할 것들 (name, shared, description)
//        // admin인지 확인
//        String name = "name";
//        SharedType shared = SHARED;
//        String description = "설명입니다";
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        String adminEmail = "admin@naver.com";
//        UserEntity adminEntity = UserEntity.of(adminEmail, password, adminName, nickname);
//
//        doThrow(new ByeolDamException(ErrorCode.INVALID_PERMISSION))
//                .when(constService.modify(eq(1L), eq(name), eq(shared), eq(description), eq(email)));
//
//        mockMvc.perform(put("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new ConstellationModifyRequest(name, shared, description)))
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    @WithMockUser
//    void 포스트수정시_수정하려는_글이_없는경우() throws Exception {
//        String title = "title";
//        String tag = "tag";
//        String description = "설명입니다";
//        DisclosureType disclosure = VISIBLE;
//
//        //mocking
//        doThrow(new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND)).when(articleService).modify(eq(1L),
//                eq(title), eq(tag), eq(description), eq(disclosure), any());
//        mockMvc.perform(put("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new ArticleModifyRequest(title, tag,
//                                description, disclosure)))
//                ).andDo(print())
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser
//    void 포스트삭제() throws Exception {
//        mockMvc.perform(delete("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithAnonymousUser
//    void 포스트삭제시_로그인하지_않은경우() throws Exception {
//        mockMvc.perform(delete("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    @WithMockUser
//    void 포스트삭제시_작성자와_삭제요청자가_다를경우() throws Exception {
//        //mocking
//        doThrow(new ByeolDamException(ErrorCode.INVALID_PERMISSION)).when(articleService).delete(any(), any());
//
//        mockMvc.perform(delete("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    @WithMockUser
//    void 포스트삭제시_삭제하려는_포스트가_존재하지_않는경우() throws Exception {
//        //mocking
//        doThrow(new ByeolDamException(ErrorCode.ARTICLE_NOT_FOUND)).when(articleService).delete(any(), any());
//
//        mockMvc.perform(delete("/api/v1/articles/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    @WithMockUser
//    void 피드목록() throws Exception {
//        when(articleService.list(any())).thenReturn(Page.empty());
//
//        mockMvc.perform(get("/api/v1/articles")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithAnonymousUser
//    void 피드목록_요청시_로그인하지_않은경우() throws Exception {
//        when(articleService.list(any())).thenReturn(Page.empty());
//
//        mockMvc.perform(get("/api/v1/articles")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

//    @Test
//    @WithMockUser
//    void 내_피드목록() throws Exception {
//        when(articleService.my(any(), any())).thenReturn(Page.empty());
//
//        mockMvc.perform(get("/api/v1/articles/my")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithAnonymousUser
//    void 내_피드목록_요청시_로그인하지_않은경우() throws Exception {
//        when(articleService.my(any(), any())).thenReturn(Page.empty());
//
//        mockMvc.perform(get("/api/v1/articles/my")
//                        .contentType(MediaType.APPLICATION_JSON)
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }

    private UserEntity getUserEntityOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ByeolDamException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
    }
}
