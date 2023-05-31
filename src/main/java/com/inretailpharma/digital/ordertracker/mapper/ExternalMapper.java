package com.inretailpharma.digital.ordertracker.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.dto.external.ActionDto;
import com.inretailpharma.digital.ordertracker.dto.external.ActionHistoryDto;
import com.inretailpharma.digital.ordertracker.dto.external.ActionSyncDto;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalOrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalSyncDto;
import com.inretailpharma.digital.ordertracker.dto.external.AuditOrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.external.AuditSyncDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

@Slf4j
@Component
public class ExternalMapper {
	
	public ExternalOrderStatusDto convertOrderStatusDtoToExternalOrderStatusDto(OrderStatusDto orderStatusDto) {
				
		return ExternalOrderStatusDto.builder()
                .statusName(orderStatusDto.getStatus())
                .statusDate(DateUtil.currentDateLong())
                .latitude(orderStatusDto.getLatitude())
                .longitude(orderStatusDto.getLongitude())
                .statusNote(orderStatusDto.getNote())
                .customNote(orderStatusDto.getDescription())
                .origin("APP")
                .code(orderStatusDto.getCode())
                .updatedBy(orderStatusDto.getUpdatedBy())
                .build();
	}
	
	public ExternalSyncDto convertSyncOrderDtoToExternalSyncDto(SyncOrderDto syncOrderDto) {		
		ExternalSyncDto externalSyncDto = new ExternalSyncDto();
		externalSyncDto.setOrderId(syncOrderDto.getEcommerceId());
		List<ExternalOrderStatusDto> history = new ArrayList<>();
		syncOrderDto.getStatusOrderHistory().forEach(orderStatus-> {		
			OrderTrackerStatus.Code code = OrderTrackerStatus.Code.parse(orderStatus.getStatus());			
			if (!OrderTrackerStatus.Code.INTERNAL.contains(code)) {
				history.add(convertOrderStatusDtoToExternalOrderStatusDto(orderStatus));				
			}
		});
		externalSyncDto.setStatusOrderHistory(history);
		return externalSyncDto;
	}

	public ActionDto convertOrderStatusDtoToActionDto(OrderStatusDto orderStatusDto) {
    	return ActionDto.builder()
    			.action(OrderTrackerStatus.Code.parse(orderStatusDto.getStatus()).getAction())
    			.orderCancelCode(orderStatusDto.getCode())
    			//.orderCancelObservation(orderStatusDto.getDescription())
    			.motorizedId(orderStatusDto.getUpdatedBy())
    			.updatedBy(orderStatusDto.getUpdatedBy())
    			.origin(Constant.APPLICATION_NAME)
    			.build();
    }
    
    public List<ActionSyncDto> convertSyncOrderDtoToSyncActionDto(List<SyncOrderDto> orders) {

    	return orders.stream().map(o ->
    		ActionSyncDto.builder()
    				.ecommerceId(o.getEcommerceId())
    				.origin(Constant.APPLICATION_NAME)
    				.history(
    						o.getStatusOrderHistory().stream()
    						.filter(f -> !OrderTrackerStatus.Code.INTERNAL.contains(f.getStatus()))
    						.map(oh ->
									{
										return ActionHistoryDto.builder()
													.action(OrderTrackerStatus.Code.parse(oh.getStatus()).getAction())
													.actionDate(DateUtil.getDateTimeFormatted(oh.getCreationDate()))
													.orderCancelCode(oh.getCode())
													//.orderCancelObservation(oh.getDescription())
													.motorizedId(oh.getUpdatedBy())
													.build();
									}).collect(Collectors.toList())
    				).build()
    	).collect(Collectors.toList());
    }
    
    public AuditOrderStatusDto convertOrderStatusDtoToAuditOrderStatusDto(OrderStatusDto orderStatusDto, boolean error) {
		log.info("[START] call to service - convertOrderStatusDtoToAuditOrderStatusDto - body:{}",
				orderStatusDto);
    	String sourceName = Optional.ofNullable(orderStatusDto.getSourceName())
    			.orElseGet(() -> Constant.APPLICATION_NAME);

    	return AuditOrderStatusDto.builder()
				.ecommerceId(orderStatusDto.getOrderExternalId())
    			.statusCode(error?
    				OrderTrackerStatus.Code.parse(orderStatusDto.getStatus()).getErrorValue() : 
    				OrderTrackerStatus.Code.parse(orderStatusDto.getStatus()).getValue())
    			.timeFromUi(DateUtil.getDateTimeFormatted(orderStatusDto.getCreationDate()))
    			.orderNote(orderStatusDto.getCode())
    			.source(sourceName)
    			.updatedBy(orderStatusDto.getUpdatedBy())
    			.latitude(Optional.ofNullable(orderStatusDto.getLatitude()).map(BigDecimal::valueOf).orElse(null))
    			.longitude(Optional.ofNullable(orderStatusDto.getLongitude()).map(BigDecimal::valueOf).orElse(null))
    			.target(Constant.APPLICATION_TARGET)
    			.statusDetail(orderStatusDto.getStatusDetail())
    			.build();
    }
    
    public List<AuditSyncDto> convertSyncOrderDtoToAuditSyncDto(List<SyncOrderDto> orders, boolean error) {
    	return orders.stream().map(o ->
    		AuditSyncDto.builder()
    				.ecommerceId(o.getEcommerceId())
    				.statusHistory(
    						o.getStatusOrderHistory().stream().map( oh ->
    							convertOrderStatusDtoToAuditOrderStatusDto(oh, error)
    						).collect(Collectors.toList())
    				).build()
    	).collect(Collectors.toList());
    }
}
