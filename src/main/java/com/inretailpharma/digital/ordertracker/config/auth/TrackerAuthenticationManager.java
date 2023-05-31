package com.inretailpharma.digital.ordertracker.config.auth;

import com.inretailpharma.digital.ordertracker.config.aditional.CustomUserDetailsService;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrackerAuthenticationManager implements AuthenticationManager {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails;
        try {

            TokenInfo tokenInfo = (TokenInfo) authentication.getDetails();
            userDetails = customUserDetailsService.loadUserByUsername(tokenInfo.getEmail());

        } catch (UsernameNotFoundException ex) {
        	log.error("UsernameNotFoundException: {}", ex.getMessage());
            return null;
        }

        return new TrackerAuthenticationToken(authentication, userDetails.getAuthorities(), true);
    }
}
