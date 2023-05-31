package com.inretailpharma.digital.ordertracker.service.order;

import java.util.List;

import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import reactor.core.publisher.Mono;

public interface ExternalOrderService {
	
	Mono<Void> kitStatusUpdate(String motorizedId, OrderStatusDto orderStatusDto, Constant.MotorizedType currentType, String updateBy);
    Mono<Void> synchronizeOrder(Long ecommercePurchaseId, SyncOrderDto syncOrderDto, Constant.MotorizedType currentType);
    Mono<Void> bulkSynchronizeOrder(List<Long> orderIds, OrderTrackerStatus.Code statusCode, Constant.MotorizedType currentType, String motorizedId, String updateBy);
}
