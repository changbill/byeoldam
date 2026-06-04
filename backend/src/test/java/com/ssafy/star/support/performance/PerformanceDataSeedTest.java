package com.ssafy.star.support.performance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@EnabledIfEnvironmentVariable(named = "PERFORMANCE_SEED", matches = "true")
class PerformanceDataSeedTest {

    @Autowired
    PerformanceDataSeeder seeder;

    @Test
    void recreateMinimalDataset() {
        PerformanceSeedResult result = seeder.recreate(PerformanceDataset.MINIMAL);
        System.out.println(result.summary());
    }

    @Test
    void recreateLargeDataset() {
        PerformanceSeedResult result = seeder.recreate(PerformanceDataset.LARGE);
        System.out.println(result.summary());
    }
}
