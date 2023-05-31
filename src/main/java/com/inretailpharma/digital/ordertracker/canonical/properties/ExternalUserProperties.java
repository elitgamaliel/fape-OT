package com.inretailpharma.digital.ordertracker.canonical.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;

@ConfigurationProperties(prefix = "external.users", ignoreInvalidFields = true)
@Getter
public class ExternalUserProperties {

    private List<String> name = new ArrayList<>(0);
    private List<String> password = new ArrayList<>(0);    
}