package com.inretailpharma.digital.ordertracker.rest.core.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
	private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    
    private static final String[] blackListRequestUri = new String[]{
		"/orders/status/assigned"
    }; 
    
    private static final String[] blackListResponseUri = new String[]{
		"/motorized/assigned-orders",
		"/drugstores",
		"/motorized/cancelled-orders-today",
		"/nvr/motorized/export",
		"/orders/status",
    };
    
    private static final String[] blackListParamResponseUri = new String[]{
    	"/locals/",
    	"/orders/local/"
    }; 
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.info(TOP_BORDER);
        log.info(" REQUEST : {} {}{}", request.getMethod(), request.getRequestURI(),getParameters(request));
        String body = ByteSource.wrap(ByteStreams.toByteArray(request.getInputStream())).asCharSource(Charsets.UTF_8).read();
        if(!StringUtils.isEmpty(body) && isLoggerUri(blackListRequestUri, request.getRequestURI())) log.info(" body: {}", body);        
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	log.info(" {}",SINGLE_DIVIDER);
    	log.info(" RESPONSE: {} {}", request.getMethod(), request.getRequestURI());
    	if ( response instanceof ResettableStreamHttpServletResponse ) {
			((ResettableStreamHttpServletResponse)response).payloadFilePrefix = ((ResettableStreamHttpServletRequest)request).payloadFilePrefix;
			((ResettableStreamHttpServletResponse)response).payloadTarget  = ((ResettableStreamHttpServletRequest)request).payloadTarget;
			writeResponse((ResettableStreamHttpServletResponse) response, request.getRequestURI());
		}
        log.info(BOTTOM_BORDER);
        super.afterCompletion(request, response, handler, ex);
    }

    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        posted.append("?");
        while (e.hasMoreElements()) {
            if (posted.length() > 1) posted.append("&");
            String curr = (String) e.nextElement();
            posted.append(curr + "=");
            posted.append(request.getParameter(curr));
        }
        String res = posted.toString(); 
        return (res.equals("?")?"":res);
    }
    
	public void writeResponse(ResettableStreamHttpServletResponse wrappedResponse, String uri){
		Integer status = wrappedResponse.getStatus();
		if(isLoggerUri(blackListResponseUri, uri) &&
				isLoggerUriParam(blackListParamResponseUri, uri)) {
			byte[] data = new byte[wrappedResponse.rawData.size()];
		    for (int i = 0; i < data.length; i++) {
		    	data[i] = (byte) wrappedResponse.rawData.get(i);
		    }
		    log.info(" status: {}, body: {}", status, new String(data));
		}else {
			log.info(" status: {}", status);
		}
	}
	
	private boolean isLoggerUri(String[] blackList, String uri) {
		boolean res = true;
		for(String itemBlackList: blackList) {
			if(itemBlackList.equals(uri)) {
				res=false;
				break;
			}
		}
		return res;
	}
	
	private boolean isLoggerUriParam(String[] blackList, String uri) {
		boolean res = true;
		for(String itemBlackList: blackList) {
			if(uri != null && uri.startsWith(itemBlackList)) {
				res=false;
				break;
			}
		}
		return res;
	}

}
