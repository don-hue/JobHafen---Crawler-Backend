package com.JobHafen.Crawler.service;

import de.TheDonJuan.dto.job.JobDto;
import de.TheDonJuan.dto.search.SearchToCrawlDto;
import com.JobHafen.Crawler.dto.TaskDTO;
import com.JobHafen.Crawler.factory.CrawlerFactory;
import com.JobHafen.Crawler.factory.CrawlerInterface;
import com.JobHafen.Crawler.producer.JobProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.*;

@Service
public class JobService {
    @Autowired
    JobProducer jobProducer;

    public Boolean crawlUrl(List<SearchToCrawlDto> searches) {
        ExecutorService crawlerPool = Executors.newFixedThreadPool(10);
        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<SearchToCrawlDto> dtoList = searches.stream()
//                    .map(search -> objectMapper.convertValue(
//                            search,
//                            SearchToCrawlDto.class
//                    ))
//                    .toList();
            List<Future<Boolean>> futures = searches.stream()
                    .flatMap(search -> search.urls().stream()
                            .map(url -> new TaskDTO(url, search)))
                    .map(task -> crawlerPool.submit(() -> {
                        try {
                            CrawlerInterface crawler =
                                    CrawlerFactory.createCrawler(
                                            task.url(),
                                            task.search()
                                    );
                            List<JobDto> jobs = crawler.crawl();
                            for (JobDto job : jobs) {
                                jobProducer.sendJobRequest(job);
                            }
                            jobProducer.confirmCrawl(task.search().searchId());
                            return true;
                        } catch (Exception e) {
                            System.err.println(
                                    "Crawler fehlgeschlagen: " + task.url()
                            );
                            e.printStackTrace();
                            return false;
                        }
                    }))
                    .toList();

            Boolean allSuccessful = true;
            for (Future<Boolean> future : futures) {
                try {
                    if (!future.get()) {
                        allSuccessful = false;
                    }
                } catch (ExecutionException e) {
                    allSuccessful = false;
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    allSuccessful = false;
                }
            }
            return allSuccessful;
        } finally {
            crawlerPool.shutdown();
        }
    }
}
