package com.JobHafen.Crawler.factory;

import com.JobHafen.Crawler.dto.SearchToCrawlDto;

public class CrawlerFactory {
    public static CrawlerInterface createCrawler(String url, SearchToCrawlDto search) {
        if(url.toLowerCase().contains("stepstone")){
            return new Stepsstone.Builder()
                    .url(url)
                    .search(search)
                    .build();
        }
        if(url.toLowerCase().contains("commerzbank")){
            return new Commerzbank.Builder()
                    .search(search)
                    .url(url)
                    .build();
        }
        if(url.toLowerCase().contains("f-i.de")){
            return new FinanzInformatik.Builder()
                    .search(search)
                    .keyword(search.keyword())
                    .build();
        }
        throw new IllegalArgumentException(url);
    }
}
