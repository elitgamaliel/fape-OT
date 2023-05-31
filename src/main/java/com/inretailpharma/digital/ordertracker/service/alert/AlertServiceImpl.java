package com.inretailpharma.digital.ordertracker.service.alert;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.entity.Alert;
import com.inretailpharma.digital.ordertracker.repository.AlertRepository;

@Service
public class AlertServiceImpl implements AlertService {

	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public List<Alert> getAlertByClient(String appname) {
		List<String> filterList = Arrays.asList(appname.split(","));
        return alertRepository.findAllByTagList(filterList);
    }

}
