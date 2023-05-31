package com.inretailpharma.digital.ordertracker.service.external.tracker;

import java.util.HashMap;
import java.util.Map;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inretailpharma.digital.ordertracker.config.ExternalTrackerConfig;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalMotorizedStatusDto;
import com.inretailpharma.digital.ordertracker.exception.NetworkException;
import com.inretailpharma.digital.ordertracker.mapper.ExternalMapper;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("inkatracker")
public class InkaTrackerService extends BaseExternalTrackerService implements ExternalTrackerService {
	
	public InkaTrackerService(ExternalTrackerConfig externalTrackerConfig,
			@Qualifier("externalRestTemplate") RestTemplate restTemplate,
			ExternalMapper mapper) {
		
		super(externalTrackerConfig, restTemplate, mapper);
	}
	
	@Override
	public Constant.MotorizedType getMotorizedType() {
		return Constant.MotorizedType.DELIVERY_CENTER;
	}

	@Override
	public void updateMotorizedStatus(String motorizedId, ExternalMotorizedStatusDto motorizedStatusDto) {
		try {
			log.info("[START] call to service - updateMotorizedStatus - uri:{} - motorizedId:{} - body:{}",
					externalTrackerConfig.getInkaTrackerProperties().getUpdateMotorizedStatusUrl(),
					motorizedId, motorizedStatusDto);
			
			Map<String, String> params = new HashMap<>();
			params.put("motorizedId", motorizedId);
			
			HttpEntity<ExternalMotorizedStatusDto> request = new HttpEntity<>(motorizedStatusDto);
			
			restTemplate.exchange(externalTrackerConfig.getInkaTrackerProperties().getUpdateMotorizedStatusUrl(),
					HttpMethod.PUT, request, Void.class, params);

	    	log.info("[END] call to service - updateMotorizedStatus"); 
		} catch (Exception ex) {
			log.error("[ERROR] call to service - updateMotorizedStatus", ex);
			throw new NetworkException(Constant.Error.UPDATE_STATUS_ERROR);
        }
	}


}
