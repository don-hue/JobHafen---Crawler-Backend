package com.JobHafen.Crawler.factory;

import com.JobHafen.Crawler.dto.JobDto;

import java.util.ArrayList;
import java.util.List;

public interface CrawlerInterface {
    default List<JobDto> crawl(){
        List<JobDto> jobs = new ArrayList<>();
        System.out.println("Default crawl needs to be overwritten");
        return jobs;
    }
}
