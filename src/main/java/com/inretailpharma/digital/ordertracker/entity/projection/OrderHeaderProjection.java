package com.inretailpharma.digital.ordertracker.entity.projection;

public interface OrderHeaderProjection {
	
	Long getEcommerceId();
	String getTrackingCode();
    String getMotorizedId();
    String getOrderStatus();
    String getServiceType();
    String getGroupName();
    String getMotorizedType();
}

