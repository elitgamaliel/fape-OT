package com.inretailpharma.digital.ordertracker.service.external.tracker;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.exception.TrackerException;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExternalTrackerFactory {
	
	@Autowired
    private List<ExternalTrackerService> services;

    private final Map<Constant.MotorizedType, ExternalTrackerService> servicesCache = new EnumMap<>(Constant.MotorizedType.class);

    @PostConstruct
    public void init() {
    	log.info("#loading externalTracker services");
        for(ExternalTrackerService service : services) {
        	log.info(service.getMotorizedType().name());
        	servicesCache.put(service.getMotorizedType(), service);
        }
    }

    public ExternalTrackerService getService(Constant.MotorizedType motorizedType) {
    	ExternalTrackerService service = servicesCache.get(motorizedType);
        if(service == null) {
        	throw new TrackerException(Constant.Error.INVALID_MOTORIZED_TYPE);
        }
        return service;
    }
}
