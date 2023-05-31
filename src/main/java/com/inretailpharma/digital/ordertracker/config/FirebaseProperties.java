package com.inretailpharma.digital.ordertracker.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@SuppressWarnings("all")
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "firebase.system.config", ignoreUnknownFields = true)
public class FirebaseProperties {

    private String orderTrackerServiceAccount;
    private String inkaTrackerServiceAccount;
    private String inkaTrackerLiteServiceAccount;
    private String orderTrackerDatabaseUrl;
    private String inkaTrackerDatabaseUrl;
    private String inkaTrackerLiteDatabaseUrl;
}
