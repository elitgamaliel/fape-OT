package com.inretailpharma.digital.ordertracker.service.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inretailpharma.digital.ordertracker.dto.in.SendSmsIn;
import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import com.inretailpharma.digital.ordertracker.exception.ExternalException;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ExternalSmsService")
public class ExternalSmsServiceImpl implements ExternalSmsService {
	
	@Autowired
	private OrderTrackerProperties orderTrackerProperties;
	
	@Autowired
	@Qualifier("externalRestTemplate")
	private RestTemplate restTemplate;

	@Override
    public void sendSms(SendSmsIn sendSmsIn) {
        log.info("sendSms {}", sendSmsIn.getPhoneNumber());
        try {
	        String notificationUrl = orderTrackerProperties.getNofificationUrl();
            restTemplate.postForLocation(notificationUrl, sendSmsIn);
        } catch (Exception e) {
            throw new ExternalException(Constant.Error.SMS_ERROR, Constant.ErrorCode.SMS_ERROR);
        }

    }
}
