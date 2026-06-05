package com.ssafy.star.support;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
public abstract class TestContainerSupport {

    private static final MongoDBContainer MONGODB = new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

    static {
        MONGODB.start();
    }

    @DynamicPropertySource
    static void registerContainerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MONGODB.getReplicaSetUrl("byeol_dam"));
    }
}
