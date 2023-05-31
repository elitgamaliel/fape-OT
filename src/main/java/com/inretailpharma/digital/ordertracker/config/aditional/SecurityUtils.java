package com.inretailpharma.digital.ordertracker.config.aditional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.inretailpharma.digital.ordertracker.exception.InactiveUserException;
import com.inretailpharma.digital.ordertracker.exception.InvalidUserException;
import com.inretailpharma.digital.ordertracker.exception.OrderTrackerException;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

/**
 * Utilidades para la seguridad
 */
public final class SecurityUtils {
	
	private SecurityUtils() {
		
	}

	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
    public static String getUID() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                TokenInfo tokenInfo = (TokenInfo) details.getDecodedDetails();
                return tokenInfo.getUserId();
            }
            return null;
        } catch (InactiveUserException | InvalidUserException ex) {
            return null;

        }
    }

    public static String getToken() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
            return details.getTokenValue();
        }else {
        	throw new OrderTrackerException("Token no valido.");
        }
    }
}
