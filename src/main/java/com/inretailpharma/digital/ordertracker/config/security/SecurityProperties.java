package com.inretailpharma.digital.ordertracker.config.security;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@ConfigurationProperties(prefix = "interceptor.security", ignoreInvalidFields = true)
@Getter
public class SecurityProperties {

    private List<String> disabledFor = new ArrayList<>(0);

}
