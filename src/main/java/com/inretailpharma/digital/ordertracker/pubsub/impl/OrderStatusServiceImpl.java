package com.inretailpharma.digital.ordertracker.pubsub.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import com.inretailpharma.digital.ordertracker.dto.external.ActionDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.pubsub.OrderStatusService;
import com.inretailpharma.digital.ordertracker.service.external.AuditTrackerService;
import com.inretailpharma.digital.ordertracker.transactions.OrderTransaction;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@EnableBinding(OrderStatusService.class)
public class OrderStatusServiceImpl {

	private OrderTransaction orderTransaction;
	private AuditTrackerService auditTrackerService;

	@Autowired
	public OrderStatusServiceImpl(OrderTransaction orderTransaction, AuditTrackerService auditTrackerService) {
		this.orderTransaction = orderTransaction;
		this.auditTrackerService = auditTrackerService;
	}

	@StreamListener(OrderStatusService.CHANNEL)
	public void handleMessage(ActionDto payload) {

		log.info("[START] OrderStatusServiceImpl - payload {}", payload);		

		if (payload != null) {
			
			OrderTrackerStatus.Code newStatus = OrderTrackerStatus.Code.parseByAction(payload.getAction());
			if (newStatus.isMessagingEnabled()) {
		    	String result = Mono.fromCallable(() -> orderTransaction.kitStatusUpdate(payload.getEcommerceId(), newStatus.name(), payload.getUpdatedBy()))
		        		.flatMap(orderStatusDto -> {
		        			
		        			if (orderStatusDto.isAuditable()) {
		        				
		        				orderStatusDto.setSourceName(Constant.ORIGIN_UNIFIED_POS);	  
		        				orderStatusDto.setUpdatedBy(payload.getUpdatedBy());
			    		        auditTrackerService.updateOrderStatus(payload.getEcommerceId(), orderStatusDto, false)
			    		        	.subscribe();
		        			}
		    		        return Mono.just(Constant.Response.SUCCESS);
		    		        
		        		}).block();
		    	
		    	log.info("[INFO] OrderStatusServiceImpl - result {}", result);	
			}
		}

		log.info("[END] OrderStatusServiceImpl - payload {}", payload);
	}

}
