package com.inretailpharma.digital.ordertracker.service.order;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus.Code;
import com.inretailpharma.digital.ordertracker.exception.UserTypeException;
import com.inretailpharma.digital.ordertracker.service.external.tracker.ExternalTrackerFactory;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.Constant.MotorizedType;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ExternalOrderServiceImpl implements ExternalOrderService {

    private ExternalTrackerFactory externalTrackerFactory;

    public ExternalOrderServiceImpl(ExternalTrackerFactory externalTrackerFactory) {
        this.externalTrackerFactory = externalTrackerFactory;
    }

    @Override
    public Mono<Void> kitStatusUpdate(String motorizedId, OrderStatusDto orderStatusDto, Constant.MotorizedType currentType, String updateBy) {
    	checkMotorizedType(orderStatusDto.getOrderExternalId(), currentType);
        
    	orderStatusDto.setUpdatedBy(updateBy);
        return externalTrackerFactory
                .getService(currentType)
                .updateOrderStatus(orderStatusDto.getOrderExternalId(), orderStatusDto);
    }
    
    @Override
	public Mono<Void> synchronizeOrder(Long ecommerceId, SyncOrderDto orderDto, Constant.MotorizedType currentType) {
    	checkMotorizedType(ecommerceId, currentType);
    	
    	orderDto.setEcommerceId(ecommerceId);
		return externalTrackerFactory
			.getService(currentType)
			.syncOrderStatus(orderDto);
    	
	}
    
    @Override
	public Mono<Void> bulkSynchronizeOrder(List<Long> orderIds, Code statusCode, MotorizedType currentType, String motorizedId, String updateBy) {
    	checkMotorizedType(0L, currentType);
    	
    	OrderStatusDto orderStatusDto = OrderStatusDto.builder()
    			.status(statusCode.name())
    			.creationDate(DateUtil.currentDateLong())
    			.origin("APP")
    			.updatedBy(updateBy)
    			.build();
    	
    	List<SyncOrderDto> orders = orderIds.stream()
    			.map(id ->  new SyncOrderDto(null, id, Collections.singletonList(orderStatusDto)))
    			.collect(Collectors.toList());
    	
    	return externalTrackerFactory
    		.getService(currentType)
    		.bulkSyncOrderStatus(orders);
    	
	}
    
    private void checkMotorizedType(Long ecommerceId, Constant.MotorizedType motorizedType) {
    	if (motorizedType == null) {
    		log.error("Motorized type null, cannot send update to the external tracker - order {}", ecommerceId);
    		throw new UserTypeException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);
    	}
    }

}
