package com.inretailpharma.digital.ordertracker.config.aditional;

import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = getAccessTokenConverter().extractAuthentication(map);

        TokenInfo tokenInfo = TokenInfo.builder()
                .userId((String) map.get(AdditionalJWTInformation.USER_ID_KEY))
                .email(AdditionalJWTInformation.EMAIL_KEY)
                .clientId((String) map.get(AdditionalJWTInformation.CLIENT_ID_KEY))
                .build();

        authentication.setDetails(tokenInfo);

        return authentication;
    }
}
