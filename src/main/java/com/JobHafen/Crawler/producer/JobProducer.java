package com.JobHafen.Crawler.producer;
import com.JobHafen.Crawler.config.RabbitMQJobConfig;
import de.TheDonJuan.dto.job.JobDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobProducer {
    private final RabbitTemplate rabbitTemplate;


    public JobProducer(@Qualifier("jobTemplate") RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJobRequest(JobDto job) {
        rabbitTemplate.convertAndSend(
                RabbitMQJobConfig.EXCHANGE,
                RabbitMQJobConfig.SAVE_JOB_ROUTING_KEY,
                job
        );
    }

    public void confirmCrawl(Long searchId) {
        rabbitTemplate.convertAndSend(
           RabbitMQJobConfig.EXCHANGE,
           RabbitMQJobConfig.CONFIRM_CRAWL_ROUTING_KEY,
           searchId
        );
    }
}
