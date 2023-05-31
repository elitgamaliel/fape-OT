package com.inretailpharma.digital.ordertracker.entity.projection;

import java.time.LocalDateTime;

public interface UserHistoryProjection {
	
	Long getId();	
	String getUserId();
	String getStatusCode();
	LocalDateTime getCreationDate();
	String getLocalCode();
	
	String getChildStatusCode();
	LocalDateTime getChildCreationDate();
}
