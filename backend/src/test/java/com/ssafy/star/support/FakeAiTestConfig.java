package com.ssafy.star.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.star.ai.application.AiService;
import com.ssafy.star.article.dto.response.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

@Configuration
@Profile("test")
public class FakeAiTestConfig {

    @Bean
    @Primary
    AiService aiService() {
        return new AiService(null, null) {
            private final ObjectMapper objectMapper = new ObjectMapper();

            @Override
            public String uploadTempImage(MultipartFile imageFile) {
                return "test://ai/" + imageFile.getOriginalFilename();
            }

            @Override
            public Response<JsonNode> connectDjango(MultipartFile imageFile) {
                JsonNode result = objectMapper.createObjectNode()
                        .put("source", "fake-ai")
                        .put("imageUrl", uploadTempImage(imageFile))
                        .put("detected", true);
                return Response.success(result);
            }
        };
    }
}
