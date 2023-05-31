package com.inretailpharma.digital.ordertracker.config.aditional;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.service.user.UserService;

@Component
public class AdditionalJWTInformation implements TokenEnhancer {

    public static final String EMAIL_KEY = "email";
    public static final String NAME_KEY = "name";
    public static final String ENABLED_KEY = "enabled";
    public static final String VALIDATED_KEY = "validated";
    public static final String USER_ID_KEY = "userId";
    public static final String NICK_NAME = "nick_name";
    public static final String USER_NAME_KEY = "user_name";
    public static final String CLIENT_ID_KEY = "client_id";
    private static final String ROLES_KEY = "roles";
    private static final String CELL_PHONE = "cellphone";
    private static final String DNI = "dni";
    private static final String LOCAL = "local_code";
    private static final String TYPE_MOTORIZED = "type_motorized";
    @Qualifier("trackerUserService")
    @Autowired
    private UserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>();
        String email = oAuth2Authentication.getName();
        User user = userService.findByEmail(email);
        info.put(USER_ID_KEY, user.getId());
        info.put(NICK_NAME, user.getAlias());
        info.put(NAME_KEY, user.getFirstName() + " " + user.getLastName());
        info.put(CELL_PHONE, user.getPhone());
        info.put(DNI, user.getDni());
        if (user.getType() != null) {
            info.put(TYPE_MOTORIZED, user.getType().getCode().name());
        }
        info.put(ROLES_KEY, oAuth2Authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        if (user.getCurrentLocal() != null) {
            info.put(LOCAL, user.getCurrentLocal());
        }
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
    }
}
