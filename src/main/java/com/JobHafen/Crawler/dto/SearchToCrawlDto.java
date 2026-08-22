package com.JobHafen.Crawler.dto;

import java.util.List;

public record SearchToCrawlDto(
        Long searchId,
        String keyword,
        List<String> urls
) {
}
