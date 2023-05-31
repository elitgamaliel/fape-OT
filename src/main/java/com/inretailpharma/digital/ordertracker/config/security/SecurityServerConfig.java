package com.inretailpharma.digital.ordertracker.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityServerConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SecurityProperties properties;

    public static final String OAUTH_TOKEN_URL = "/oauth/token";

    @Override
    public void configure(WebSecurity web) {
        List<String> list = new ArrayList<>();
        list.addAll(properties.getDisabledFor());
        web.ignoring().antMatchers(HttpMethod.OPTIONS)
                .and().ignoring().antMatchers(list.toArray(new String[0]))
                .and().ignoring().antMatchers("/card-payment-detail/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, OAUTH_TOKEN_URL).permitAll();
    }
}
