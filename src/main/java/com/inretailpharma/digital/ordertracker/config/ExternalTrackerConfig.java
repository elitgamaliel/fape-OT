package com.inretailpharma.digital.ordertracker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
public class ExternalTrackerConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "external-tracker.properties.inka-tracker")
	public ExternalTrackerProperties getInkaTrackerProperties() {
	    return new ExternalTrackerProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "external-tracker.properties.inka-tracker-lite")
	public ExternalTrackerProperties getInkaTrackerLiteProperties() {
	    return new ExternalTrackerProperties();
	}
	
	@Bean
	@ConfigurationProperties(prefix = "external-tracker.properties.delivery-manager")
	public ExternalTrackerProperties getDeliveryManagerProperties() {
	    return new ExternalTrackerProperties();
	}

	@Data
	public static class ExternalTrackerProperties {
		private UpdateOrderStatus updateOrderStatus;
		private String updateMotorizedStatusUrl;
		private BulkSyncOrderStatus bulkSyncOrderStatus;
	}


	@Data
	public static class UpdateOrderStatus
	{
		private String updateOrderStatusUrl;
		private String updateOrderStatusConnectTimeout;
		private String updateOrderStatusReadTimeout;

	}



	@Data
	public static class BulkSyncOrderStatus
	{
		private String bulkSyncOrderStatusUrl;
		private String bulkSyncOrderStatusConnectTimeout;
		private String bulkSyncOrderStatusReadTimeout;

	}


}
