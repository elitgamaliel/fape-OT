package com.inretailpharma.digital.ordertracker.facade;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.OrderDistanceAudit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.TrackerReason;
import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.exception.OrderException;
import com.inretailpharma.digital.ordertracker.exception.OrderFinishedException;
import com.inretailpharma.digital.ordertracker.mapper.ObjectToMapper;
import com.inretailpharma.digital.ordertracker.service.external.AuditTrackerService;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.service.order.ExternalOrderService;
import com.inretailpharma.digital.ordertracker.service.order.OrdertrackerService;
import com.inretailpharma.digital.ordertracker.service.system.ProjectedEtaService;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.transactions.OrderTransaction;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class OrderFacade {

    private OrderTransaction orderTransaction;
    private ObjectToMapper objectToMapper;
    private OrdertrackerService ordertrackerService;
    private ProjectedEtaService projectedEtaService;
    private ExternalOrderService extOrderService;
    private ExternalService externalService;
    private AuditTrackerService auditTrackerService;
    private ExternalOrderService extTrackerOrderService;
    private UserService userService;

    
    public OrderFacade(
    		OrderTransaction orderTransaction, ObjectToMapper objectToMapper,
    		OrdertrackerService ordertrackerService, ProjectedEtaService projectedEtaService,
    		ExternalOrderService extOrderService, ExternalService externalService,
    		AuditTrackerService auditTrackerService, ExternalOrderService extTrackerOrderService,
    		@Qualifier("trackerUserService") UserService userService) {
    	
    	this.orderTransaction = orderTransaction;
    	this.objectToMapper = objectToMapper;
    	this.ordertrackerService = ordertrackerService;
    	this.projectedEtaService = projectedEtaService;
    	this.extOrderService = extOrderService;
    	this.externalService = externalService;
    	this.auditTrackerService = auditTrackerService;
    	this.extTrackerOrderService = extTrackerOrderService;
    	this.userService = userService;
    }
    

    public void save(OrderDto orderDto) {
    	log.info("save to order {}",orderDto);
    	try {
    		orderTransaction.createOrder(objectToMapper.convertOrderDtoToOrderTrackerEntity(orderDto));
    	} catch (Exception ex) {
        	log.error("Error while inserting the order {}", orderDto.getEcommerceId(), ex);
        	throw new OrderException(Constant.MESSAGE_ERROR, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Mono<String> kitStatusUpdate(Long ecommerceId, String status, String updateBy) {
    	
    	return Mono.fromCallable(() -> orderTransaction.kitStatusUpdate(ecommerceId, status, updateBy))
    		.flatMap(orderStatusDto -> {    			
    			
    			orderStatusDto.setSourceName(Constant.SOURCE_DELIVERY_MANAGER);
    			
		        auditTrackerService.updateOrderStatus(ecommerceId, orderStatusDto, false)
		        	.subscribe();
		        
		        return Mono.just(Constant.Response.SUCCESS);
    		});

    }
    
    public Mono<String> statusUpdateLocation(String motorizedId, OrderStatusDto orderStatusDto, String updateBy) {

    	return Mono.fromCallable(() -> orderTransaction.statusUpdateLocation(motorizedId, orderStatusDto, updateBy))
    		.flatMap(header -> {
    				
    			if ((!StringUtils.isEmpty(orderStatusDto.getOrigin()) && orderStatusDto.getOrigin().equals("APP")) 
    		       	&& (!OrderTrackerStatus.Code.ON_HOLD.name().equals(orderStatusDto.getStatus()) )) {
    		       		
    		       		Constant.MotorizedType currentType = Constant.MotorizedType.parseByName(header.getMotorizedType());
    		       		extTrackerOrderService.kitStatusUpdate(motorizedId, orderStatusDto, currentType, updateBy)
    		       			.subscribe();
   		        	           
   		        }
				orderStatusDto.setUpdatedBy(motorizedId);
   		        auditTrackerService.updateOrderStatus(header.getEcommerceId(), orderStatusDto, false)
   		        	.subscribe();
   				
   		        return Mono.just(Constant.Response.SUCCESS);
   			});	
    }

    public List<TrackerReasonDto> getAllTransferReason(TrackerReason.Type type) {
    	if (TrackerReason.Type.REJECTED.equals(type)) {
    		
    		return externalService.getCancellationReason().stream()
    				.map(objectToMapper::convertCancellationReasonDtoToTrackerReasonDto).collect(Collectors.toList());
    	}
    	return Collections.emptyList();
    }
    
    public Mono<AssignedOrdersResponseDto> assignOrders(ProjectedGroupDto projectedGroupDto) {
    	log.info("## assigning orders - group {}", projectedGroupDto.getGroupName());    	
    	 	
    	final OrderTrackerStatus.Code code;
    	final Constant.MotorizedType motorizedType;

    	if (Constant.SOURCE_EXTERNAL_ROUTER.equalsIgnoreCase(projectedGroupDto.getSource())) {
    		code = OrderTrackerStatus.Code.EXT_ASSIGNED;
        	motorizedType = Constant.MotorizedType.DRUGSTORE;    		
    	} else {
    		code = OrderTrackerStatus.Code.ASSIGNED;
        	motorizedType = Constant.MotorizedType.DELIVERY_CENTER;
    	}

    	return Mono.fromCallable(()-> orderTransaction.bulkCreateOrder(projectedGroupDto.getGroup()))
	    	.flatMap(result -> {

	    		List<Long> failedOrdersIds = result.getFailedOrders().stream().map(FailedOrderDto::getOrderId).collect(Collectors.toList());
	    		log.info("## created orders {} - {}", result.getCreatedOrders().size(), result.getCreatedOrders());
	    		log.info("## failed orders {} - {}", failedOrdersIds.size(), failedOrdersIds);
	    		
	    		if (!result.getCreatedOrders().isEmpty()) {
	    			
	    			recalculatePositions(projectedGroupDto, failedOrdersIds);

					Optional<String> localCode = projectedGroupDto.getGroup().stream().findFirst().map(g -> g.getOrder().getLocalCode());
					if (localCode.isPresent()) {
						DrugstoreDto local = externalService.findDrugstore(localCode.get());
						Optional.ofNullable(local).ifPresent(l -> {					
							PickUpCenterDto pickUpCenter = new PickUpCenterDto();
							pickUpCenter.setLatitude(l.getLatitude());
							pickUpCenter.setLongitude(l.getLongitude());
							pickUpCenter.setName(l.getName());
				            projectedGroupDto.setPickUpCenter(pickUpCenter);
						});
					} else {
						log.error("[ERROR] group {} with no localCode", projectedGroupDto.getGroupName());
					}
					
					orderTransaction.assignOrders(projectedGroupDto, false, null, motorizedType, code);
					
					auditTrackerService.bulkSyncOrderStatus(result.getCreatedOrders(), code, projectedGroupDto.getUpdateBy(), Constant.SOURCE_DELIVERY_MANAGER)
                		.subscribe();
					
					result.setAssigmentSuccessful(Constant.Response.SUCCESS);				

	    		} else {
	    			
	    			result.setAssigmentSuccessful(Constant.Response.ERROR);	
	    			result.setMessage("Failed to create all the orders");	
	    		}
	    		
	    		return Mono.just(result);

	    	})
			.onErrorResume(ex -> {
				
				Optional.ofNullable(projectedGroupDto.getGroup())
				.ifPresent(gs -> {
					
					List<Long> orderIds = projectedGroupDto.getGroup().stream().map(GroupDto::getOrderId).collect(Collectors.toList());
					auditTrackerService.bulkSyncOrderStatus(orderIds, code, null, projectedGroupDto.getSource(), ex.getMessage(), true).subscribe();
					
				});
				log.error("Error while assigning orders - group {}", projectedGroupDto.getGroupName(), ex);
    	
				return Mono.just(new AssignedOrdersResponseDto(Collections.emptyList(), Collections.emptyList(), Constant.Response.ERROR, ex.getMessage()));
			});
    	
    }

	public Mono<String> unassignOrders(UnassignedDto unassignedDto) {
		return Mono.fromCallable(() -> orderTransaction.unassignOrders(unassignedDto))
	        	.flatMap(orderIds -> {

	                auditTrackerService.bulkSyncOrderStatus(orderIds, OrderTrackerStatus.Code.UNASSIGNED, unassignedDto.getUpdateBy(), unassignedDto.getSource())
	                	.subscribe();
	        		
	        		return Mono.just(Constant.Response.SUCCESS);
	        	});
    }
	

	public Mono<String> createTravel(TravelDto travelDto, String motorizedId, String updatedBy, Constant.MotorizedType motorizedType) {

		ordertrackerService.validateTravel(travelDto, motorizedId);		
		ProjectedGroupDto projectedGroupDto = projectedEtaService.projectedEtaInTravel(travelDto);
		projectedGroupDto.setMotorizedId(motorizedId);        
		
        return Mono.fromCallable(() -> orderTransaction.assignOrders(projectedGroupDto, true, updatedBy, motorizedType, OrderTrackerStatus.Code.ASSIGNED))
        	.flatMap(orderIds -> {
        		
        		extTrackerOrderService.bulkSynchronizeOrder(orderIds, OrderTrackerStatus.Code.ASSIGNED, motorizedType, projectedGroupDto.getMotorizedId(), updatedBy)
        			.subscribe();
                auditTrackerService.bulkSyncOrderStatus(orderIds, OrderTrackerStatus.Code.ASSIGNED, updatedBy, null)
                	.subscribe();
        		
        		return Mono.just(Constant.Response.SUCCESS);
        	});
    }	
	
	public Mono<String> cancelTravel(String motorizedId, String updatedBy, Constant.MotorizedType motorizedType) {
		
		return Mono.fromCallable(() -> orderTransaction.cancelTravel(motorizedId, updatedBy))
	        	.flatMap(orderIds -> {
	        		
	        		extTrackerOrderService.bulkSynchronizeOrder(orderIds, OrderTrackerStatus.Code.PREPARED , motorizedType, motorizedId, updatedBy)
	        			.subscribe();
	                auditTrackerService.bulkSyncOrderStatus(orderIds, OrderTrackerStatus.Code.PREPARED, updatedBy, null)
	                	.subscribe();
	        		return Mono.just(Constant.Response.SUCCESS);
	        	});
    }
	
	public Mono<String> nvrCreateTravel(TravelDto travelDto, String motorizedId, String updatedBy) {
		Constant.MotorizedType motorizedType = userService.getCurrentMotorizedType(motorizedId);
		if (motorizedType == null) {
			log.error("The motorized with id {} does not have a type assigned", motorizedId);
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
		}
		return this.createTravel(travelDto, motorizedId, updatedBy, motorizedType);
	}
	
	public Mono<String> nvrCancelTravel(String motorizedId, String updatedBy) {
		Constant.MotorizedType motorizedType = userService.getCurrentMotorizedType(motorizedId);
		if (motorizedType == null) {
			log.error("The motorized with id {} does not have a type assigned", motorizedId);
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
		}
		return cancelTravel(motorizedId, updatedBy, motorizedType);
	}
	
	public ProjectedGroupDto helpGroupOrder(TravelDto travelDto) {
		return projectedEtaService.projectedEtaInTravel(travelDto);
    }
	
	public Flux<SyncOrderResponseDto> synchronizeOrders(List<SyncOrderDto> orders, String motorizedId) {

    	orders.stream().forEach(order->
    		order.getStatusOrderHistory().stream().forEach(status->
    			status.setUpdatedBy(motorizedId)
    		)
    	);
    	
    	return Flux.fromIterable(orders)
    		.flatMap(order -> {
    			
    			SyncOrderResponseDto response = new SyncOrderResponseDto();
    			response.setTrackingCode(order.getTrackingCode());
    			try {	
    				OrderUpdateDto updateResult = orderTransaction.synchronizeOrder(order, motorizedId);
					
    				extOrderService.synchronizeOrder(updateResult.getOrderId(), order, updateResult.getCurrentType())
    					.subscribe();
    				auditTrackerService.syncOrderStatus(updateResult.getOrderId(), order)
    					.subscribe();
    				
    				response.setResult(Constant.Response.SUCCESS);
    			} catch (OrderFinishedException ex) {
    				response.setResult(Constant.ErrorCode.ORDER_FINALIZED_ERROR);
    			} catch (Exception ex) {
    				log.error("Error while synchronising the order {} ", order.getTrackingCode() , ex);
    				response.setResult(Constant.Response.ERROR);
    			}
    			
    			return Mono.just(response);
    		});
	}
	
	private void recalculatePositions(ProjectedGroupDto projectedGroupDto, List<Long> failedOrdersIds) {
		if (!failedOrdersIds.isEmpty()) {
			AtomicInteger pos = new AtomicInteger(1);
			
			List<GroupDto> newGroup = projectedGroupDto.getGroup().stream()
					.filter(f -> !failedOrdersIds.contains(f.getOrderId()))
					.sorted(Comparator.comparingInt(GroupDto::getPosition))
					.collect(Collectors.toList());
			
			newGroup.forEach(g -> 
				g.setPosition(pos.getAndIncrement())
			);

			projectedGroupDto.setGroup(newGroup);
		}
	}

	public Mono<String> saveAuditDistance(String UID, OrderDistanceAuditDto orderDistanceAuditDto)
	{
		try {
			OrderDistanceAudit order = objectToMapper.convertOrderDistanceAuditTrackerEntity(UID, orderDistanceAuditDto);
			auditTrackerService.saveAuditDistance(order).subscribe();
			return Mono.just(Constant.Response.SUCCESS);
		}
		catch (Exception e)
		{
			log.error("Error saving distance Audit", e);
			return Mono.just(Constant.Response.ERROR);
		}
	}


}
