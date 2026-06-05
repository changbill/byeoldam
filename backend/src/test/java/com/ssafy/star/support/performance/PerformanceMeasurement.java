package com.ssafy.star.support.performance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

record PerformanceMeasurement(
        String name,
        int warmUp,
        int iterations,
        List<PerformanceSample> samples
) {
    PerformanceMeasurement {
        samples = List.copyOf(samples);
    }

    double averageMillis() {
        return samples.stream()
                .mapToDouble(PerformanceMeasurement::toMillis)
                .average()
                .orElse(0.0);
    }

    double minMillis() {
        return samples.stream()
                .mapToDouble(PerformanceMeasurement::toMillis)
                .min()
                .orElse(0.0);
    }

    double maxMillis() {
        return samples.stream()
                .mapToDouble(PerformanceMeasurement::toMillis)
                .max()
                .orElse(0.0);
    }

    double p50Millis() {
        return percentileMillis(50);
    }

    double p95Millis() {
        return percentileMillis(95);
    }

    double p99Millis() {
        return percentileMillis(99);
    }

    double averageQueryCount() {
        return samples.stream()
                .mapToLong(PerformanceSample::queryCount)
                .average()
                .orElse(0.0);
    }

    long minQueryCount() {
        return samples.stream()
                .mapToLong(PerformanceSample::queryCount)
                .min()
                .orElse(0L);
    }

    long maxQueryCount() {
        return samples.stream()
                .mapToLong(PerformanceSample::queryCount)
                .max()
                .orElse(0L);
    }

    String markdownRow() {
        return "| %s | LARGE | %.2f | %.2f | %.2f | %.2f | %.2f | %.2f | %.1f | %d-%d |"
                .formatted(
                        name,
                        averageMillis(),
                        p50Millis(),
                        p95Millis(),
                        p99Millis(),
                        minMillis(),
                        maxMillis(),
                        averageQueryCount(),
                        minQueryCount(),
                        maxQueryCount()
                );
    }

    private double percentileMillis(int percentile) {
        if (samples.isEmpty()) {
            return 0.0;
        }
        List<Long> values = new ArrayList<>(samples.stream()
                .map(PerformanceSample::elapsedNanos)
                .toList());
        Collections.sort(values);
        int index = (int) Math.ceil(percentile / 100.0 * values.size()) - 1;
        index = Math.max(0, Math.min(index, values.size() - 1));
        return values.get(index) / 1_000_000.0;
    }

    private static double toMillis(PerformanceSample sample) {
        return sample.elapsedNanos() / 1_000_000.0;
    }
}
