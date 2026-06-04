package com.ssafy.star.support;

import com.ssafy.star.common.infra.S3.S3uploader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

@Configuration
@Profile("test")
public class FakeS3TestConfig {

    @Bean
    @Primary
    S3uploader s3uploader() {
        return new S3uploader(null) {
            @Override
            public String upload(MultipartFile multipartFile, String dirName) {
                return fakeUrl(multipartFile, dirName);
            }

            @Override
            public String uploadThumbnail(MultipartFile multipartFile, String dirName) {
                return fakeUrl(multipartFile, dirName);
            }

            @Override
            public String uploadProfile(MultipartFile multipartFile, String dirName) {
                return fakeUrl(multipartFile, dirName);
            }

            @Override
            public void deleteImageFromS3(String filePath) {
                // no-op in tests
            }

            private String fakeUrl(MultipartFile multipartFile, String dirName) {
                return "test://s3/" + dirName + "/" + multipartFile.getOriginalFilename();
            }
        };
    }
}
