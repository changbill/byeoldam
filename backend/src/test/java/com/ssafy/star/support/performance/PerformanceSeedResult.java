package com.ssafy.star.support.performance;

record PerformanceSeedResult(
        PerformanceDataset dataset,
        int users,
        int follows,
        int articles,
        int constellations,
        int comments,
        int articleLikes,
        int constellationLikes,
        String baselineUserEmail,
        String heavyArticleOwnerNickname,
        String heavyConstellationOwnerNickname
) {
    String summary() {
        return """
                dataset=%s users=%d follows=%d articles=%d constellations=%d comments=%d articleLikes=%d constellationLikes=%d baselineUserEmail=%s heavyArticleOwnerNickname=%s heavyConstellationOwnerNickname=%s
                """.formatted(
                dataset,
                users,
                follows,
                articles,
                constellations,
                comments,
                articleLikes,
                constellationLikes,
                baselineUserEmail,
                heavyArticleOwnerNickname,
                heavyConstellationOwnerNickname
        ).strip();
    }
}
