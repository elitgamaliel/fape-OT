package com.inretailpharma.digital.ordertracker.service.alert;

import java.util.List;

import com.inretailpharma.digital.ordertracker.entity.Alert;

public interface AlertService {
	public List<Alert> getAlertByClient(String appname);
}
