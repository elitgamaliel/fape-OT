package com.inretailpharma.digital.ordertracker.rest.core.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestFilter implements Filter {
    
    /**
     * Metodo que modifica el request y response para poder utilizarlo mas de una vez, especificamente en los logs del requestBody y responseBody
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ResettableStreamHttpServletRequest wrappedRequest = null;
        ResettableStreamHttpServletResponse wrappedResponse = null;
        try {
            wrappedRequest = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
            wrappedResponse = new ResettableStreamHttpServletResponse((HttpServletResponse) response);
        } catch (Exception e) {
            log.error("Fail to wrap request and response",e);
        }
        chain.doFilter(wrappedRequest, wrappedResponse);
    }
}
