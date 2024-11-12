package com.sap.coordinatedhttprequests.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration class.
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * Bean definition for RestTemplate.
     *
     * @return a RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
