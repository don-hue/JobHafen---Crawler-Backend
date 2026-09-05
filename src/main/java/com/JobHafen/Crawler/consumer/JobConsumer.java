package com.JobHafen.Crawler.consumer;

import com.JobHafen.Crawler.config.RabbitMQJobConfig;
import de.TheDonJuan.dto.search.SearchToCrawlDto;
import com.JobHafen.Crawler.service.JobService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobConsumer {
    @Autowired
    JobService jobService;

    @RabbitListener(
            queues = RabbitMQJobConfig.CRAWL_SEARCHES_REQUEST_QUEUE,
            containerFactory = "jobListenerFactory"
    )
    public Boolean crawlSearches(List<SearchToCrawlDto> searches) {
        return jobService.crawlUrl(searches);
    }
}
