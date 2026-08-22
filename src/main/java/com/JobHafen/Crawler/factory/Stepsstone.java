package com.JobHafen.Crawler.factory;


import com.JobHafen.Crawler.dto.CompanyDto;
import com.JobHafen.Crawler.dto.JobDto;
import com.JobHafen.Crawler.dto.SearchToCrawlDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stepsstone implements CrawlerInterface{
    private final String url;
    private final SearchToCrawlDto search;

    public Stepsstone(Builder builder) {
        this.url = builder.url;
        this.search = builder.search;
    }


    public static class Builder{
        private String url;
        private SearchToCrawlDto search;

        public  Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder search(SearchToCrawlDto search) {
            this.search = search;
            return this;
        }
        public Stepsstone build(){
            return new Stepsstone(this);
        }
    }
    public List<JobDto> crawl(){
        List<JobDto> jobs = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.0")
                        .timeout(5000)
                        .get();
                Elements articles = doc.select("article");

                for (Element article : articles) {
                    Element jobDiv = article.selectFirst("h2");
                    Element companyDiv = article.selectFirst("span");

                    if(jobDiv == null || companyDiv == null){
                        continue;
                    }

                    String jobTitle = jobDiv.text().trim();
                    String companyName = companyDiv.text().trim();

                    if(jobTitle.isEmpty() || companyName.isEmpty()) {
                        continue;
                    }

                    CompanyDto companyDto = new CompanyDto(
                            companyName,
                            null,
                            null,
                            true
                    );

                    JobDto jobDto = new JobDto(
                            jobTitle,
                            false,
                            companyDto,
                            search.searchId()
                    );

                    jobs.add(jobDto);
                }
            } catch (IOException e) {
                throw new RuntimeException("Crawl failed" + url, e);
            }
            return jobs;
    }
}
