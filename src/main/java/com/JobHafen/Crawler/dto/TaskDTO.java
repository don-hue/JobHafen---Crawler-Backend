package com.JobHafen.Crawler.dto;
import de.TheDonJuan.dto.search.SearchToCrawlDto;

public record TaskDTO(
        String url,
        SearchToCrawlDto search
) {
}
