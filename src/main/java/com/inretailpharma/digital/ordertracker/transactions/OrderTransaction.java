package com.inretailpharma.digital.ordertracker.transactions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderCanonical;
import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrderTracker;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrdertrackerItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.projection.OrderHeaderProjection;
import com.inretailpharma.digital.ordertracker.exception.OrderException;
import com.inretailpharma.digital.ordertracker.mapper.ObjectToMapper;
import com.inretailpharma.digital.ordertracker.service.order.FirebaseOrderService;
import com.inretailpharma.digital.ordertracker.service.order.OrdertrackerService;
import com.inretailpharma.digital.ordertracker.service.system.DeliveryTravelService;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
@Component
public class OrderTransaction {

    private OrdertrackerService ordertrackerService;
    private DeliveryTravelService deliveryTravelService;
    private FirebaseOrderService firebaseOrderService;
    private UserService firebaseUserService;
    private UserService trackerUserService;
    private UserService extTrackerUserService;
    private ObjectToMapper objectToMapper;

    @Autowired
    private OrdertrackerService orderRepositoryService;

    public OrderTransaction(@Qualifier("TrackerOrderService") final OrdertrackerService ordertrackerService
            , final DeliveryTravelService deliveryTravelService
            , final FirebaseOrderService firebaseOrderService
            , @Qualifier("firebaseUserService") final UserService firebaseUserService
            , @Qualifier("trackerUserService") final UserService trackerUserService
            , @Qualifier("externalTrackerUserService") final UserService extTrackerUserService
            , ObjectToMapper objectToMapper) {
        this.ordertrackerService = ordertrackerService;
        this.deliveryTravelService = deliveryTravelService;
        this.firebaseOrderService = firebaseOrderService;
        this.firebaseUserService = firebaseUserService;
        this.trackerUserService = trackerUserService;
        this.extTrackerUserService = extTrackerUserService;
        this.objectToMapper = objectToMapper;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void createOrder(OrderTracker orderTracker) {
        ordertrackerService.save(orderTracker);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public AssignedOrdersResponseDto bulkCreateOrder(List<GroupDto> group) {
    	
    	List<Long> createdOrders = new ArrayList<>();
    	List<FailedOrderDto> failedOrders = new ArrayList<>();

    	group.forEach(g -> {
    		
    		try {

        		this.createOrder(objectToMapper.convertOrderDtoToOrderTrackerEntity(g.getOrder()));  
        		createdOrders.add(g.getOrderId());
        		
        	} catch (Exception ex) {
        		
        		failedOrders.add(new FailedOrderDto(g.getOrderId(), ex.getMessage()));
        		log.error("[ERROR] assign orders to external tracker {} - " , g.getOrderId(), ex);   
        		
        	}
    		
    	});
    	
    	return new AssignedOrdersResponseDto(createdOrders, failedOrders, null, null);        
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public OrderStatusDto kitStatusUpdate(Long ecommerceId, String status, String updateBy) {
    		
   		return ordertrackerService.findOrderHeaderByEcommerceId(ecommerceId)
   			.map(header -> {
   				
   				OrderStatusDto orderStatusDto = new OrderStatusDto();
   				orderStatusDto.setOrderExternalId(ecommerceId);
                orderStatusDto.setOrderTrackingCode(header.getTrackingCode());
                orderStatusDto.setStatus(status);
                   
                OrderTrackerStatus.Code newCode = OrderTrackerStatus.Code.parse(orderStatusDto.getStatus());
                OrderTrackerStatus.Code currentCode = OrderTrackerStatus.Code.parse(header.getOrderStatus());

                if (OrderTrackerStatus.Code.CANCELLED.equals(newCode)  && OrderTrackerStatus.Code.CANCELLED.equals(currentCode)) {
                    log.error("# Order with ecommerceId: {} already cancelled", ecommerceId);
                    throw new OrderException(Constant.Error.ORDER_CANCELLED, Constant.ErrorCode.DEFAULT, HttpStatus.BAD_REQUEST);
                }
                    
                this.kitStatusUpdate(header, orderStatusDto, newCode, header.getMotorizedId(), updateBy);
                orderStatusDto.setAuditable(true);
    				
    			return orderStatusDto;
    		}).orElseGet(() -> {
    			OrderStatusDto orderStatusDto = new OrderStatusDto();
   				orderStatusDto.setOrderExternalId(ecommerceId);
                orderStatusDto.setStatus(status);
                orderStatusDto.setCreationDate(DateUtil.convertDatetimeToMilliseconds(LocalDateTime.now()));
                orderStatusDto.setStatusDetail(Constant.Error.NOT_ORDER);
                orderStatusDto.setAuditable(false);
                return orderStatusDto;
    		});
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public OrderHeaderProjection statusUpdateLocation(String motorizedId, OrderStatusDto orderStatusDto, String updateBy) {
    	
		return ordertrackerService.findOrderHeaderByTrackingCode(orderStatusDto.getOrderTrackingCode())
    		.map(header -> {
    			
    			OrderTrackerStatus.Code newcode = OrderTrackerStatus.Code.parse(orderStatusDto.getStatus());
                OrderTrackerStatus.Code currentCode = OrderTrackerStatus.Code.parse(header.getOrderStatus());

                if (OrderTrackerStatus.Code.NOT_IN_TRAVEL.contains(currentCode)) {
					log.error("Order {} is not in travel({}), cannot get the status {}", header.getEcommerceId(), header.getOrderStatus(), orderStatusDto.getStatus());
				
					throw new OrderException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
				}

				if (currentCode.equals(newcode)) {
					log.error("Order {} already has the status {}", header.getEcommerceId(), newcode.name());
					throw new OrderException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.SAME_ORDER_STATUS_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				this.kitStatusUpdate(header, orderStatusDto, newcode, motorizedId, updateBy); 
    				
    			return header;
    		}).orElseThrow(() -> new OrderException(Constant.Error.NOT_ORDER, Constant.ErrorCode.DEFAULT, HttpStatus.BAD_REQUEST));
    }

    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void kitStatusUpdate(OrderHeaderProjection header, OrderStatusDto orderStatusDto
    		, OrderTrackerStatus.Code status, String motorizedId, String updateBy) {
    	
    	LocalDateTime now = LocalDateTime.now();
    	orderStatusDto.setOrderExternalId(header.getEcommerceId());
        orderStatusDto.setPreviousStatus(header.getOrderStatus());
        orderStatusDto.setCreationDate(DateUtil.convertDatetimeToMilliseconds(now));
        ordertrackerService.kitStatusUpdate(header, status, motorizedId, now);
        deliveryTravelService.checkIfDeliveryTravelIsFinalizedByMotorized(motorizedId);
        
        if (status.equals(OrderTrackerStatus.Code.ON_ROUTE)) {
        	Optional.ofNullable(header.getGroupName()).ifPresent(deliveryTravelService::startTravel);
            trackerUserService.updateMotorizedStatus(motorizedId, orderStatusDto.getStatus());
            extTrackerUserService.updateMotorizedStatus(motorizedId, orderStatusDto.getStatus());
        }
        
        Constant.MotorizedType currentType = Constant.MotorizedType.parseByName(header.getMotorizedType());
        
        firebaseOrderService.kitStatusUpdate(motorizedId, orderStatusDto, currentType, updateBy);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public List<Long> assignOrders(ProjectedGroupDto projectedGroupDto, boolean updateExternal, String updatedBy, Constant.MotorizedType motorizedType, OrderTrackerStatus.Code code) {
    	
    	if (projectedGroupDto.getMotorizedId() == null) {
    		Optional.ofNullable(projectedGroupDto.getMotorizedEmail())
        	.ifPresent(mail -> {
        		Optional.ofNullable(trackerUserService.findByEmail(mail))
        		.ifPresent(u -> projectedGroupDto.setMotorizedId(u.getId()));
        	});
    	}

    	deliveryTravelService.validateNewGroupName(projectedGroupDto.getGroupName());
    	
    	HashSet<Long> uniqueIds=new HashSet<>();
    	projectedGroupDto.getGroup().removeIf(o->!uniqueIds.add(o.getOrderId()));
    	
    	List<Long> orderIds = projectedGroupDto.getGroup().stream()
        		.map(GroupDto::getOrderId)
                .collect(Collectors.toList());
        
        Map<Long, OrderTracker> ordersMap = ordertrackerService.getAllOrdersByEcommercePurchaseId(orderIds)
        		.stream().collect(Collectors.toMap(
        				OrderTracker::getEcommercePurchaseId,
	  		    		ot -> ot,
	  		    		(o1, o2) -> {
	  		    		  log.info(">>> duplicate order found {}", o2.getEcommercePurchaseId());
	  		              return o1;
	  		            }
	  		    ));

        deliveryTravelService.markLastDeliveryTravelAsFinalized(projectedGroupDto.getMotorizedId());

        deliveryTravelService.createDeliveryTravel(projectedGroupDto, ordersMap, code);

        trackerUserService.updateMotorizedStatus(projectedGroupDto.getMotorizedId(), Status.Code.PACKING.name());

        if (updateExternal) {
        	extTrackerUserService.updateMotorizedStatus(projectedGroupDto.getMotorizedId(), Status.Code.PACKING.name());
        }
        
        projectedGroupDto.getGroup().stream()
	    	.filter(f -> ordersMap.containsKey(f.getOrderId()))
	    	.forEach(group -> {
	    		OrderTracker order = ordersMap.get(group.getOrderId());
	    		group.setOrderTrackingCode(order.getOrderTrackingCode());
	    		if (order.getAddressTracker() != null) {
	    			group.setAddressLocationDto(new AddressLocationDto(order.getAddressTracker().getLatitude(),
	    					order.getAddressTracker().getLongitude()));
	            }
	    	});
        
        firebaseOrderService.assignOrders(projectedGroupDto, motorizedType.name(), code);
        
        return orderIds;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public List<Long> unassignOrders(UnassignedDto unassignedDto) {
        List<OrderTracker> orders = ordertrackerService.unassignOrders(unassignedDto.getGroupName(), unassignedDto.getOrders());
        List<OrderTracker> remainingOrders = orders.stream().filter(order -> !OrderTrackerStatus.Code.DISCARDED.contains(order.getOrderTrackerStatus().getCode())).collect(Collectors.toList());

        log.info("# Updated DeliveryTravel in group [{}] with remaining {} orders", unassignedDto.getGroupName(), remainingOrders.size());
        if (CollectionUtils.isEmpty(remainingOrders)) {
            log.info("# Cancel DeliveryTravel because all orders where unassigned");
            deliveryTravelService.cancelDeliveryTravel(unassignedDto.getGroupName());
            trackerUserService.updateMotorizedStatus(unassignedDto.getMotorizedId(), Status.Code.WAITING.name());
            firebaseUserService.updateMotorizedStatus(unassignedDto.getMotorizedId(), Status.Code.WAITING.name());
        } else {
            log.info("# Recalculate projected eta for group: {}", unassignedDto.getGroupName());
            Optional<DeliveryTravel> deliveryTravelInfo = deliveryTravelService.findDeliveryTravelByGroupName(unassignedDto.getGroupName());
            deliveryTravelInfo.ifPresent(deliveryTravel -> {

                AtomicInteger pos = new AtomicInteger(1);
                List<GroupDto> newGroupList = remainingOrders.stream()
                        .map(order -> new GroupDto(pos.getAndIncrement(), order.getExternalPurchaseId(), order.getOrderTrackingCode()))
                        .collect(Collectors.toList());

                deliveryTravelService.updateDeliveryTravelEta(deliveryTravel, remainingOrders, newGroupList);
            });
        }
        ordertrackerService.bulkUpdateOrderStatus(unassignedDto.getOrders(), OrderTrackerStatus.Code.UNASSIGNED);

        List<String> listOrderTrackingCodes = new ArrayList<>();
        List<OrderTracker> orderTrackersUnassigned = ordertrackerService.getUnassignedOrdersByIds(unassignedDto.getOrders());
        unassignedDto.getOrders().forEach(orderEcommerceId -> {
            for (OrderTracker orderTracker : orderTrackersUnassigned) {
                if (orderTracker.getEcommercePurchaseId().toString().equals(orderEcommerceId.toString())) {
                    listOrderTrackingCodes.add(orderTracker.getOrderTrackingCode());
                }
            }
        });

        firebaseOrderService.unassingOrders(listOrderTrackingCodes);         
        
        return unassignedDto.getOrders();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public List<Long> cancelTravel(String motorizedId, String updatedBy) {

    	DeliveryTravel deliveryTravel = deliveryTravelService.cancelDeliveryTravelForMotorized(motorizedId);

        List<String> listOrderTrackingCodes = deliveryTravel.getDetailList().stream()
                .map(detail -> detail.getOrderTracker().getOrderTrackingCode())
                .collect(Collectors.toList());

        List<Long> listEcommerceIds = deliveryTravel.getDetailList().stream()
                .filter( order -> !OrderTrackerStatus.Code.FINALIZED.contains(order.getOrderTracker().getOrderTrackerStatus().getCode()) )
                .map(detail -> detail.getOrderTracker().getEcommercePurchaseId())
                .collect(Collectors.toList());

        ordertrackerService.cancelTravel(deliveryTravel);
        ordertrackerService.bulkUpdateOrderStatus(listEcommerceIds, OrderTrackerStatus.Code.UNASSIGNED);
        trackerUserService.updateMotorizedStatus(motorizedId, Status.Code.WAITING.name());
        extTrackerUserService.updateMotorizedStatus(motorizedId, Status.Code.WAITING.name());        
        firebaseOrderService.unassingOrders(listOrderTrackingCodes);
        firebaseUserService.updateMotorizedStatus(motorizedId, Status.Code.WAITING.name());

        return listEcommerceIds;
        
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public OrderUpdateDto synchronizeOrder(SyncOrderDto orderDto, String motorizedId) {    	
    	
    	List<OrderStatusDto> sortedOrderStatusHistoryDto = orderDto.getStatusOrderHistory().stream()
        		.sorted(Comparator.comparingLong(OrderStatusDto::getCreationDate))
    			.collect(Collectors.toList());    	
    	
    	sortedOrderStatusHistoryDto.forEach(dto -> {
    		dto.setOrigin("APP");
    		dto.setCreatedOffline(true);
    	});
    	
    	OrderUpdateDto updateResult = ordertrackerService
    			.updateOrderHistory(orderDto.getTrackingCode(), motorizedId, sortedOrderStatusHistoryDto);
    	
    	Optional<String> optGroupName = Optional.ofNullable(updateResult.getGroupName());
    	    	
    	sortedOrderStatusHistoryDto.forEach(orderStatusDto -> {      		
    		orderStatusDto.setOrderTrackingCode(orderDto.getTrackingCode());
    		orderStatusDto.setOrderExternalId(updateResult.getOrderId());
    		orderStatusDto.setPreviousStatus(updateResult.getPreviousStatus());
    		OrderTrackerStatus.Code status = OrderTrackerStatus.Code.parse(orderStatusDto.getStatus());

    		if (status.equals(OrderTrackerStatus.Code.ON_ROUTE)) {
    			trackerUserService.updateMotorizedStatus(motorizedId, status.name());
    			optGroupName.ifPresent(deliveryTravelService::startTravel);
    		}
    		
    		firebaseOrderService.kitStatusUpdate(motorizedId, orderStatusDto, updateResult.getCurrentType(), motorizedId);
    	});
    	
    	optGroupName.ifPresent(groupName -> deliveryTravelService.checkIfDeliveryTravelIsFinalizedByGroup(groupName, motorizedId)); 
    	return updateResult;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public OrderCanonical updatePartialOrder(PartialOrderDto partialOrderDto) {

        log.info("EcommercePurchaseId():{}",partialOrderDto.getEcommercePurchaseId());
        IOrderTracker iOrderTracker = getOrderByecommerceId(partialOrderDto.getEcommercePurchaseId());
        log.info("OrderId():{}",iOrderTracker.getOrderId());
        List<IOrdertrackerItem> iOrderTrackerItem = getOrderItemByOrderTrackerId(iOrderTracker.getOrderId());
        log.info("OrderTrackerId:{}",iOrderTrackerItem.get(0).getOrderTrackerId());
        orderRepositoryService.updatePartialOrderDetail(partialOrderDto, iOrderTrackerItem);
        orderRepositoryService.updatePartialOrderHeader(partialOrderDto);
        orderRepositoryService.updatePaymentMethod(partialOrderDto,iOrderTrackerItem.get(0).getOrderTrackerId());
        IOrderTracker orderUpdated = this.getOrderByecommerceId(partialOrderDto.getEcommercePurchaseId());
        log.info("The order {} was updated sucessfully ", orderUpdated.getOrderTrackerId());
        return objectToMapper.convertIOrderDtoToOrderTrackerCanonical(orderUpdated);

    }

    public IOrderTracker getOrderByecommerceId(Long ecommerceId) {
        log.info("***ecommerceId{}:",ecommerceId);
        return orderRepositoryService.getOrderByecommerceId(ecommerceId);
    }

    public List<IOrdertrackerItem> getOrderItemByOrderTrackerId(Long orderFulfillmentId) {
        return orderRepositoryService.getOrderItemByOrderTrackerId(orderFulfillmentId);
    }
}
