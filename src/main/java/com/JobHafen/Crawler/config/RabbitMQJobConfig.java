package com.JobHafen.Crawler.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQJobConfig {
    public static final String EXCHANGE = "jobs.exchange";
    public static final String SAVE_JOB_QUEUE = "jobs.request.saveJob.queue";
    public static final String SAVE_JOB_ROUTING_KEY = "jobs.saveJob.request";
    public static final String CRAWL_SEARCHES_REQUEST_QUEUE = "jobs.request.crawlSearches.queue";
    public static final String CRAWL_SEARCHES_ROUTING_KEY = "jobs.crawlSearches.request";
    public static final String CONFIRM_CRAWL_REQUEST_QUEUE = "search.request.confirmCrawl.queue";
    public static final String CONFIRM_CRAWL_ROUTING_KEY = "search.confirmCrawl.request";

    @Bean
    public Queue saveSearchQueue() {
        return new Queue(SAVE_JOB_QUEUE);
    }
    @Bean
    public Queue crawlSearchesQueue() {
        return new Queue(CRAWL_SEARCHES_REQUEST_QUEUE);
    }
    @Bean
    public Queue confirmCrawlQueue(){return new Queue(CONFIRM_CRAWL_REQUEST_QUEUE);}

    @Bean
    public MessageConverter jobMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public DirectExchange jobExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding saveSearchBinding(
            @Qualifier("saveSearchQueue") Queue queue,
            DirectExchange jobExchange) {
        return BindingBuilder
                .bind(queue)
                .to(jobExchange)
                .with(SAVE_JOB_ROUTING_KEY);
    }
    @Bean
    public Binding crawlSearchesBinding(
            @Qualifier("crawlSearchesQueue") Queue queue,
            DirectExchange jobExchange) {
        return BindingBuilder
                .bind(queue)
                .to(jobExchange)
                .with(CRAWL_SEARCHES_ROUTING_KEY);
    }

    @Bean
    public Binding confirmCrawlBinding(
            @Qualifier("confirmCrawlQueue") Queue queue,
            DirectExchange jobExchange
    ) {
        return BindingBuilder
                .bind(queue)
                .to(jobExchange)
                .with(CONFIRM_CRAWL_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate jobTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jobMessageConverter());
        template.setReplyTimeout(10000);
        return template;
    }
    @Bean
    public SimpleRabbitListenerContainerFactory jobListenerFactory(
            ConnectionFactory connectionFactory,
            @Qualifier("jobMessageConverter")
            MessageConverter messageConverter) {

        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);

        return factory;
    }
}
