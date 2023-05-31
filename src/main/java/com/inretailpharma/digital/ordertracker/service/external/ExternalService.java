package com.inretailpharma.digital.ordertracker.service.external;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import com.inretailpharma.digital.ordertracker.dto.CancellationReasonDto;
import com.inretailpharma.digital.ordertracker.dto.DrugstoreDto;
import com.inretailpharma.digital.ordertracker.dto.OrderFulfillmentDto;
import com.inretailpharma.digital.ordertracker.exception.NetworkException;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalService {

    private OrderTrackerProperties orderTrackerProperties;
    private RestTemplate restTemplate;

    public ExternalService(
    		OrderTrackerProperties orderTrackerProperties, 
    		@Qualifier("externalRestTemplate") RestTemplate restTemplate) {
        this.orderTrackerProperties = orderTrackerProperties;
        this.restTemplate = restTemplate;
    }
    
    public List<DrugstoreDto> findAllDrugstores() {
		try {
			log.info("[START] call to service - findAllDrugstores - uri:{}",
	    			orderTrackerProperties.getFulfillmentCenterGetStoreUrl());
			
			Map<String, String> params = new HashMap<>();
			params.put("localCode", "");
			
	    	ResponseEntity<List<DrugstoreDto>> response = 
	    			restTemplate.exchange(orderTrackerProperties.getFulfillmentCenterGetStoreUrl(),
	    			HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<DrugstoreDto>>() {}, params);	
	    	log.info("[END] call to service - findAllDrugstores"); 
	    	return response.getBody();
		} catch (Exception ex) {
			log.error("[ERROR] call to service - findAllDrugstores", ex);
			throw new NetworkException(Constant.Error.DRUGSTORE_ERROR);
        } 
	}

    public List<DrugstoreDto> findAllDrugstores(String localType) {
		try {
			log.info("[START] call to service - findAllDrugstores - uri:{} - localType:{}",
	    			orderTrackerProperties.getFulfillmentCenterGetAllStoresUrl(), localType);
			
			Map<String, String> params = new HashMap<>();
			params.put("localType", localType);
			
	    	ResponseEntity<List<DrugstoreDto>> response = 
	    			restTemplate.exchange(orderTrackerProperties.getFulfillmentCenterGetAllStoresUrl(),
	    			HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<DrugstoreDto>>() {}, params);	
	    	log.info("[END] call to service - findAllDrugstores"); 
	    	return response.getBody();
		} catch (Exception ex) {
			log.error("[ERROR] call to service - findAllDrugstores", ex);
			throw new NetworkException(Constant.Error.DRUGSTORE_ERROR);
        } 
	}
    
    public DrugstoreDto findDrugstore(String localCode) {
		try {
			log.info("[START] call to service - findDrugstore - uri:{}",
	    			orderTrackerProperties.getFulfillmentCenterGetStoreUrl());
			
			Map<String, String> params = new HashMap<>();
			params.put("localCode", localCode);
			
	    	ResponseEntity<DrugstoreDto> response = 
	    			restTemplate.exchange(orderTrackerProperties.getFulfillmentCenterGetStoreUrl(),
	    			HttpMethod.GET, HttpEntity.EMPTY, DrugstoreDto.class, params);	
	    	log.info("[END] call to service - findAllDrugstores"); 
	    	return response.getBody();
		} catch (Exception ex) {
			log.error("[ERROR] call to service - findDrugstore", ex);
			throw new NetworkException(Constant.Error.DRUGSTORE_ERROR);
        } 
	}
    
    public OrderFulfillmentDto getOrderNumber(Long orderNumber) {
		try {
			log.info("[START] call to service - getOrderNumber - uri:{}", orderTrackerProperties.getDeliverymanagerGetOrderUrl());
			Map<String, Long> params = new HashMap<>();
			params.put("orderNumber", orderNumber);
		
	    	ResponseEntity<OrderFulfillmentDto> response = 
	    			restTemplate.exchange(orderTrackerProperties.getDeliverymanagerGetOrderUrl(),
	    			HttpMethod.GET, HttpEntity.EMPTY, OrderFulfillmentDto.class, params);	
	    	log.info("[END] call to service - getOrderNumber"); 
	    	return response.getBody();
		} catch (Exception ex) {
			log.error("[ERROR] call to service - getOrderNumber", ex);
			throw new NetworkException(Constant.Error.ORDER_ERROR);
        } 
	}
    
    public List<CancellationReasonDto> getCancellationReason() {
		try {
			log.info("[START] call to service - getCancellationReason - uri:{}", orderTrackerProperties.getFulfillmentCenterGetCancellationReasonUrl());
			Map<String, String> params = new HashMap<>();
			
	    	ResponseEntity<List<CancellationReasonDto>> response = 
	    			restTemplate.exchange(orderTrackerProperties.getFulfillmentCenterGetCancellationReasonUrl(),
	    			HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<CancellationReasonDto>>() {}, params);	
	    	
	    	log.info("[END] call to service - getCancellationReason"); 
	    	return response.getBody();
		} catch (Exception ex) {
			log.error("[ERROR] call to service - getCancellationReason", ex);
			throw new NetworkException(Constant.Error.ORDER_ERROR);
        } 
	}
}
