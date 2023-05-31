package com.inretailpharma.digital.ordertracker.config;


import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inretailpharma.digital.ordertracker.firebase.exception.FirebaseException;
import com.inretailpharma.digital.ordertracker.firebase.service.InkaTrackerFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.service.InkaTrackerLiteFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.service.OrderTrackerFirebaseService;

@Configuration
@SuppressWarnings("all")
@ConditionalOnProperty({"firebase.system.config.order-tracker-service-account"
        , "firebase.system.config.inka-tracker-service-account"
        , "firebase.system.config.inka-tracker-lite-service-account"})
@EnableConfigurationProperties({FirebaseProperties.class})
public class FirebaseConfig {

    private final ApplicationContext context;
    private final FirebaseProperties properties;
    private FirebaseApp orderTrackerFirebaseApp;
    private FirebaseApp inkaTrackerFirebaseApp;
    private FirebaseApp inkaTrackerLiteFirebaseApp;

    public FirebaseConfig(final ApplicationContext context, final FirebaseProperties properties) {
        this.context = context;
        this.properties = properties;

        final String ln = System.getProperty("line.separator");
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.info("Load Inkafarma configuration class [\n\t{}\n]", String.join(ln.concat("\t"),
                ClassUtils.getAbbreviatedName(FirebaseConfig.class, 32)));
    }

    @PostConstruct
    public void initializeOrderTrackerFirebase() {

        Resource resource = context.getResource(properties.getOrderTrackerServiceAccount());
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .setDatabaseUrl(properties.getOrderTrackerDatabaseUrl())
                    .build();
            orderTrackerFirebaseApp = FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void initializeInkaTrackerFirebase() {
        Resource resource = context.getResource(properties.getInkaTrackerServiceAccount());
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .setDatabaseUrl(properties.getInkaTrackerDatabaseUrl())
                    .build();
            inkaTrackerFirebaseApp = FirebaseApp.initializeApp(options, "inkaTrackerFirebaseApp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void initializeInkaTrackerLiteFirebase() {
        Resource resource = context.getResource(properties.getInkaTrackerLiteServiceAccount());
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .setDatabaseUrl(properties.getInkaTrackerLiteDatabaseUrl())
                    .build();
            inkaTrackerLiteFirebaseApp = FirebaseApp.initializeApp(options, "inkaTrackerLiteFirebaseApp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FirebaseAuth orderTrackerFirebaseAuth() {
        return FirebaseAuth.getInstance(orderTrackerFirebaseApp);
    }

    @Bean
    public FirebaseAuth inkaTrackerFirebaseAuth() {
        return FirebaseAuth.getInstance(inkaTrackerFirebaseApp);
    }

    @Bean
    public FirebaseAuth inkaTrackerLiteFirebaseAuth() {
        return FirebaseAuth.getInstance(inkaTrackerLiteFirebaseApp);
    }

    @Bean
    @ConditionalOnProperty("firebase.system.config.order-tracker-database-url")
    public DatabaseReference orderTrackerDatabase() {
        return FirebaseDatabase.getInstance(orderTrackerFirebaseApp).getReference();
    }

    @Bean
    @ConditionalOnProperty("firebase.system.config.inka-tracker-database-url")
    public DatabaseReference inkaTrackerDatabase() {
        return FirebaseDatabase.getInstance(inkaTrackerFirebaseApp).getReference();
    }

    @Bean
    @ConditionalOnProperty("firebase.system.config.inka-tracker-lite-database-url")
    public DatabaseReference inkaTrackerLiteDatabase() {
        return FirebaseDatabase.getInstance(inkaTrackerLiteFirebaseApp).getReference();
    }

    @Bean
    @ConditionalOnBean(DatabaseReference.class)
    public OrderTrackerFirebaseService orderTrackerFirebaseService() {
        return new OrderTrackerFirebaseService();
    }

    @Bean
    @ConditionalOnBean(DatabaseReference.class)
    public InkaTrackerFirebaseService inkaTrackerFirebaseService() {
        return new InkaTrackerFirebaseService();
    }

    @Bean
    @ConditionalOnBean(DatabaseReference.class)
    public InkaTrackerLiteFirebaseService inkaTrackerLiteFirebaseService() {
        return new InkaTrackerLiteFirebaseService();
    }

    @Bean
    public GoogleCredential orderTrackerFirebaseAuthToken() {
        GoogleCredential scoped = null;
        try {
            Resource resource = context.getResource(properties.getOrderTrackerServiceAccount());
            scoped = GoogleCredential.fromStream(resource.getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.database",
                            "https://www.googleapis.com/auth/userinfo.email"));

        } catch (IOException e) {
            throw new FirebaseException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return scoped;
    }

    @Bean
    public GoogleCredential inkaTrackerFirebaseAuthToken() {
        GoogleCredential scoped = null;
        try {
            Resource resource = context.getResource(properties.getInkaTrackerServiceAccount());
            scoped = GoogleCredential.fromStream(resource.getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.database",
                            "https://www.googleapis.com/auth/userinfo.email"));

        } catch (IOException e) {
            throw new FirebaseException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return scoped;
    }

    @Bean
    public GoogleCredential inkaTrackerLiteFirebaseAuthToken() {
        GoogleCredential scoped = null;
        try {
            Resource resource = context.getResource(properties.getInkaTrackerLiteServiceAccount());
            scoped = GoogleCredential.fromStream(resource.getInputStream())
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.database",
                            "https://www.googleapis.com/auth/userinfo.email"));

        } catch (IOException e) {
            throw new FirebaseException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return scoped;
    }
}
