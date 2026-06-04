package com.ssafy.star.support.performance;

enum PerformanceDataset {
    MINIMAL(5, 10, 50, 10, 100, 100, 100, 5, 10, 10),
    LARGE(100, 1_000, 10_000, 1_000, 10_000, 10_000, 10_000, 50, 1_000, 100);

    private final int userCount;
    private final int followCount;
    private final int articleCount;
    private final int constellationCount;
    private final int commentCount;
    private final int articleLikeCount;
    private final int constellationLikeCount;
    private final int focusedFollowCount;
    private final int focusedArticleCount;
    private final int focusedConstellationCount;

    PerformanceDataset(
            int userCount,
            int followCount,
            int articleCount,
            int constellationCount,
            int commentCount,
            int articleLikeCount,
            int constellationLikeCount,
            int focusedFollowCount,
            int focusedArticleCount,
            int focusedConstellationCount
    ) {
        this.userCount = userCount;
        this.followCount = followCount;
        this.articleCount = articleCount;
        this.constellationCount = constellationCount;
        this.commentCount = commentCount;
        this.articleLikeCount = articleLikeCount;
        this.constellationLikeCount = constellationLikeCount;
        this.focusedFollowCount = focusedFollowCount;
        this.focusedArticleCount = focusedArticleCount;
        this.focusedConstellationCount = focusedConstellationCount;
    }

    int userCount() {
        return userCount;
    }

    int followCount() {
        return followCount;
    }

    int articleCount() {
        return articleCount;
    }

    int constellationCount() {
        return constellationCount;
    }

    int commentCount() {
        return commentCount;
    }

    int articleLikeCount() {
        return articleLikeCount;
    }

    int constellationLikeCount() {
        return constellationLikeCount;
    }

    int focusedFollowCount() {
        return focusedFollowCount;
    }

    int focusedArticleCount() {
        return focusedArticleCount;
    }

    int focusedConstellationCount() {
        return focusedConstellationCount;
    }
}
