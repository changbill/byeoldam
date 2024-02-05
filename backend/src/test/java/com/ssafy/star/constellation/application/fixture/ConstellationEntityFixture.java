package com.ssafy.star.constellation.application.fixture;

import com.ssafy.star.article.DisclosureType;
import com.ssafy.star.article.domain.ArticleEntity;
import com.ssafy.star.constellation.SharedType;
import com.ssafy.star.constellation.domain.ConstellationEntity;
import com.ssafy.star.user.domain.UserEntity;

public class ConstellationEntityFixture {
    public static ConstellationEntity get(String name, SharedType shared, String description, UserEntity adminEntity) {

        ConstellationEntity entity = ConstellationEntity.of(name, shared, description, adminEntity);
        return entity;
    }
}