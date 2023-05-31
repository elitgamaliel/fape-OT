package com.inretailpharma.digital.ordertracker.config;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaAuditing
public class TrackerConfig {
	
	@Value( "${service.timeout.connect}" )
	private int connectTimeout;
	
	@Value( "${service.timeout.read}" )
	private int readTimeout;
	
    @Bean(name = "externalRestTemplate")
    public RestTemplate createExternalRestTemplate(RestTemplateBuilder restTemplateBuilder) {
    	log.info("Reading timeout config - connect: {} - read: {}", connectTimeout, readTimeout);
    	return restTemplateBuilder
    			.setConnectTimeout(Duration.ofMillis(connectTimeout))
    			.setReadTimeout(Duration.ofMillis(readTimeout))
    			.build();
   }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
