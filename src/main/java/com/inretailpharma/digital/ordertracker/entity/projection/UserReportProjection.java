package com.inretailpharma.digital.ordertracker.entity.projection;

public interface UserReportProjection {

	String getId();
	String getDni();
	String getFirstName();
	String getLastName();
	String getAlias();
	String getPhone();
	Long getAssignedOrder();
}
