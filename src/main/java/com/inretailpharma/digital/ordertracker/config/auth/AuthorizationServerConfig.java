package com.inretailpharma.digital.ordertracker.config.auth;


import com.inretailpharma.digital.ordertracker.config.aditional.AdditionalJWTInformation;
import com.inretailpharma.digital.ordertracker.config.aditional.CustomJwtAccessTokenConverter;
import com.inretailpharma.digital.ordertracker.firebase.service.OrderTrackerFirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.util.Base64Utils;

import javax.sql.DataSource;
import java.util.Arrays;


@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String PERMIT_ALL = "permitAll()";
    private static final String IS_AUTHENTICATED = "isAuthenticated()";

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private OrderTrackerFirebaseService firebaseService;

    private TrackerAuthenticationManager authenticationManager;

    @Autowired
    private JwtConfig jwtConfig;

    private AdditionalJWTInformation additionalJWTInformation;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(getPasswordEncoder());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess(PERMIT_ALL)
                .checkTokenAccess(IS_AUTHENTICATED);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(additionalJWTInformation, accessTokenConverter()));
        FirebaseTokenGranter firebaseTokenGranter = new FirebaseTokenGranter(endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(),
                authenticationManager, firebaseService);

        endpoints.accessTokenConverter(accessTokenConverter())
                .tokenGranter(firebaseTokenGranter)
                .tokenEnhancer(tokenEnhancerChain);
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        byte[] privateKey = Base64Utils.decodeFromString(jwtConfig.getPrivateKey());
        byte[] publicKey = Base64Utils.decodeFromString(jwtConfig.getPublicKey());
        JwtAccessTokenConverter accessTokenConverter = new CustomJwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(new String(privateKey));
        accessTokenConverter.setVerifierKey(new String(publicKey));
        return accessTokenConverter;
    }

    @Autowired
    public void setAuthenticationManager(TrackerAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setAdditionalJWTInformation(AdditionalJWTInformation additionalJWTInformation) {
        this.additionalJWTInformation = additionalJWTInformation;
    }
}
