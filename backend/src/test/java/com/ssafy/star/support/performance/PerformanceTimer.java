package com.ssafy.star.support.performance;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Component
class PerformanceTimer {

    private final Statistics statistics;

    PerformanceTimer(EntityManagerFactory entityManagerFactory) {
        this.statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        this.statistics.setStatisticsEnabled(true);
    }

    PerformanceMeasurement measure(String name, int warmUp, int iterations, Runnable target) {
        for (int index = 0; index < warmUp; index++) {
            target.run();
        }

        List<PerformanceSample> samples = new ArrayList<>(iterations);
        for (int index = 0; index < iterations; index++) {
            statistics.clear();
            long startedAt = System.nanoTime();
            target.run();
            long elapsedNanos = System.nanoTime() - startedAt;
            samples.add(new PerformanceSample(elapsedNanos, statistics.getPrepareStatementCount()));
        }
        return new PerformanceMeasurement(name, warmUp, iterations, samples);
    }
}
