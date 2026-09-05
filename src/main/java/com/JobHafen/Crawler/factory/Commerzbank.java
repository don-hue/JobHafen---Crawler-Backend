package com.JobHafen.Crawler.factory;
import de.TheDonJuan.dto.CompanyDto;
import de.TheDonJuan.dto.job.JobDto;
import de.TheDonJuan.dto.search.SearchToCrawlDto;
import org.htmlunit.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Commerzbank implements CrawlerInterface{

    private final String url;
    private final SearchToCrawlDto search;
    public Commerzbank(Builder builder){
        this.url = builder.url;
        this.search = builder.search;
    }

    public static class Builder {
        private String url;
        private SearchToCrawlDto search;


        public Builder url(String url){
            this.url = url;
            return this;
        }

        public Builder search(SearchToCrawlDto search) {
            this.search =search;
            return  this;
        }

        public Commerzbank build() {
            return new Commerzbank(this);
        }
    }

    public List<JobDto> crawl(){
        List<JobDto> jobDtoList = new ArrayList<>();
        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false); // faster
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            final String jobsJson = webClient
                    .getPage(url)
                    .getWebResponse().getContentAsString();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jobsJson);
            JsonNode jobs = root.path("SearchResult")
                    .path("SearchResultItems");

            for (JsonNode job : jobs) {
                String title = job.get("MatchedObjectDescriptor")
                        .get("PositionTitle")
                        .asText();

                JsonNode location = job.path("MatchedObjectDescriptor")
                        .path("PositionLocation");

                String cityName = location.get(0)
                        .path("CityName")
                        .asText();

                CompanyDto company = new CompanyDto(
                        "Commerzbank - " + cityName,
                        null,
                        null,
                        true
                );



                JobDto jobDto = new JobDto(
                        title,
                        false,
                        company,
                        search.searchId()
                );

                jobDtoList.add(jobDto);
            }
        } catch (IOException e) {
            System.out.println("Error accessing: ");
            throw new RuntimeException();
        }
        return jobDtoList;
    }
}
