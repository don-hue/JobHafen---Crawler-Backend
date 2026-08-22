package com.JobHafen.Crawler.dto;

import java.net.URL;

public record CompanyDto (
        String companyName,
        URL homepage,
        URL api,
        boolean showCompany
) {
}
