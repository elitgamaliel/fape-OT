package com.inretailpharma.digital.ordertracker.config.aditional;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inretailpharma.digital.ordertracker.rest.core.interceptor.LogInterceptor;

@Configuration
public class EndpointsConfiguration implements WebMvcConfigurer {

    @Bean
    public GsonHttpMessageConverter gsonHttpMessageConverter() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .registerTypeAdapter(DefaultOAuth2AccessToken.class, new AccessTokenAdapter())
                .create();

        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();


        gsonHttpMessageConverter.setGson(gson);

        return gsonHttpMessageConverter;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
    }

}
