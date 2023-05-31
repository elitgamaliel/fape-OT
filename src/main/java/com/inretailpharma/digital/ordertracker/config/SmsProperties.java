package com.inretailpharma.digital.ordertracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sms.properties")
@Component
@Data
public class SmsProperties {
    private String messageCodeFarmamoto;
}
