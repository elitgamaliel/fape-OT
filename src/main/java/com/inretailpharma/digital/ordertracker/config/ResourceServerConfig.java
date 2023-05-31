package com.inretailpharma.digital.ordertracker.config;

import com.inretailpharma.digital.ordertracker.config.security.UserStatusFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    public static final String PATCH_ENDPOINT_LOGIN = "/oauth/token";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.PUT, PATCH_ENDPOINT_LOGIN).permitAll()
                .anyRequest().authenticated()
                .and().addFilterAfter(userStatusFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public Filter userStatusFilter() {
        return new UserStatusFilter();
    }
}
