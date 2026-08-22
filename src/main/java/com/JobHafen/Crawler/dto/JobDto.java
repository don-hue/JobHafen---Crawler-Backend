package com.JobHafen.Crawler.dto;

public record JobDto(
        String jobTitle,
        boolean applied,
        CompanyDto company,
        Long searchId
        ) {}
