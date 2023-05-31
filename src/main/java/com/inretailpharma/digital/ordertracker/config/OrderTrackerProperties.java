package com.inretailpharma.digital.ordertracker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "tracker.properties.external", ignoreUnknownFields = true)
@Component
@Data
public class OrderTrackerProperties {

    private String nofificationUrl;
    private String fulfillmentCenterGetStoreUrl;
    private String fulfillmentCenterGetAllStoresUrl;
    private String deliverymanagerGetOrderUrl;
    private String auditCreateOrderStatusHistoryUrl;
    private String auditCreateOrderStatusHistoryBulkUrl;
    private String fulfillmentCenterGetCancellationReasonUrl;
    private String updateMethodPaymentUrl;
    private String confirmAttendanceUrl;
    private String pubsubPublishingUrl;
}
