package com.JobHafen.Crawler.factory;
import com.JobHafen.Crawler.config.Constants;
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

public class FinanzInformatik implements CrawlerInterface{
    private final String keyword;
    private final SearchToCrawlDto search;


    private FinanzInformatik(Builder builder) {
        this.keyword = builder.keyword;
        this.search = builder.search;
    }

    public static class Builder {
        private String keyword;
        private SearchToCrawlDto search;

        public Builder keyword(String keyword){
            this.keyword =keyword;
            return  this;
        }

        public Builder search(SearchToCrawlDto search) {
            this.search = search;
            return this;
        }

        public FinanzInformatik build(){
            return new FinanzInformatik(this);
        }
    }

    @Override
    public List<JobDto> crawl() {
        List<JobDto> jobs = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(Constants.FinanzInformatik_Jobpage).get();
            Elements items = doc.select("div.list-row div.list-item");

            for (Element item : items) {
                if(item.text().toLowerCase().contains(keyword.toLowerCase())) {

                    CompanyDto companyDto = new CompanyDto(
                            Constants.FI_COMPANY_NAME,
                            null,
                            null,
                            true
                    );

                    JobDto jobDto = new JobDto(
                            item.text(),
                            false,
                            companyDto,
                            search.searchId()
                    );
                    jobs.add(jobDto);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage() );
            throw new RuntimeException();
        }
        return jobs;
    }

}
