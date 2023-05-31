package com.inretailpharma.digital.ordertracker.service.external.tracker;

import java.util.List;

import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalMotorizedStatusDto;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import reactor.core.publisher.Mono;

public interface ExternalTrackerService {
	
	Constant.MotorizedType getMotorizedType();
	Mono<Void> updateOrderStatus(Long orderId, OrderStatusDto orderStatusDto);
	void updateMotorizedStatus(String motorizedId, ExternalMotorizedStatusDto motorizedStatusDto);
	Mono<Void> syncOrderStatus(SyncOrderDto order);
	Mono<Void> bulkSyncOrderStatus(List<SyncOrderDto> orders);
}
