package com.inretailpharma.digital.ordertracker.service.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrderTracker;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrdertrackerItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.inretailpharma.digital.ordertracker.canonical.tracker.MotorizedCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderDetailCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderMotorizedCanonical;
import com.inretailpharma.digital.ordertracker.entity.Client;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import com.inretailpharma.digital.ordertracker.entity.MotorizedType;
import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.PaymentMethod;
import com.inretailpharma.digital.ordertracker.entity.ServiceType;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.TrackerReason;
import com.inretailpharma.digital.ordertracker.entity.projection.OrderHeaderProjection;
import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.exception.OrderException;
import com.inretailpharma.digital.ordertracker.exception.OrderFinishedException;
import com.inretailpharma.digital.ordertracker.mapper.OrderTrackerMapper;
import com.inretailpharma.digital.ordertracker.repository.ClientRepository;
import com.inretailpharma.digital.ordertracker.repository.DeliveryTravelRepository;
import com.inretailpharma.digital.ordertracker.repository.OrdertrackerRepository;
import com.inretailpharma.digital.ordertracker.repository.TrackerReasonRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.service.parameter.ParameterServiceImpl;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import com.inretailpharma.digital.ordertracker.utils.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;


/**
 * Implementacion interna de {@link OrdertrackerService}
 * . Esta clase no se debe acceder directamente
 *
 * @author
 */

@Slf4j
@Service("TrackerOrderService")
public class OrdertrackerServiceImpl implements OrdertrackerService {

	@Autowired
    private OrdertrackerRepository ordertrackerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TrackerReasonRepository trackerReasonRepository;

    @Autowired
    private DeliveryTravelRepository deliveryTravelRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ExternalService externalService;
    
    @Autowired
    private ParameterServiceImpl parameterService;

    @Autowired
    private OrdertrackerRepository orderRepository;

    @Override
    public void save(OrderTracker orderTracker) {
        log.info("INSERT  /orderTraker: {}", orderTracker.getEcommercePurchaseId());
        if (!ordertrackerRepository.existsByEcommercePurchaseId(orderTracker.getEcommercePurchaseId())) {
            Client cliente = clientRepository.save(orderTracker.getClient());
            orderTracker.setClient(null);
            orderTracker.setClient(cliente);
            ordertrackerRepository.saveAndFlush(orderTracker);
        } else {
            log.info("Order {} already exists", orderTracker.getEcommercePurchaseId());
        }
    }

    @Override
    public Flux<OrderDto> findOrders(Pageable pageable, Long drugstore, List<OrderTrackerStatus.Code> status) {
        List<OrderTrackerStatus> list = new ArrayList<>();
        for (OrderTrackerStatus.Code code : status) {
            OrderTrackerStatus orderTrackerStatus = new OrderTrackerStatus();
            orderTrackerStatus.setCode(code);
            list.add(orderTrackerStatus);
        }
        List<OrderTracker> orderTrackers = ordertrackerRepository.findByOrderTrackerStatusIn(list);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (OrderTracker orderTracker : orderTrackers) {
            orderDtos.add(OrderTrackerMapper.mapOrderTrackerToDto(orderTracker));
        }
        return Flux.fromIterable(orderDtos);
    }

    @Override
    public OrderDto findOrderByEcommercePurchaseId(Long orderId) {
        return OrderTrackerMapper.mapOrderTrackerToDto(ordertrackerRepository.findByEcommercePurchaseId(orderId));
    }

    @Override
    public OrderDto findOrderTrackingCode(String orderTrackingCode) {
        return OrderTrackerMapper.mapOrderTrackerToDto(ordertrackerRepository.findByOrderTrackingCode(orderTrackingCode));
    }

    @Override
    public OrderHeaderProjection kitStatusUpdate(OrderHeaderProjection header, OrderTrackerStatus.Code status, String motorizedId, LocalDateTime date) {
    	ordertrackerRepository.updateStatus(header.getEcommerceId(), status.name(), date);  
    	return header;
    }

    @Override
    public List<TrackerReason> getAllTrackerReasonByType(TrackerReason.Type type) {
        return trackerReasonRepository.findAllByType(type);
    }

    @Override
    public List<OrderDto> findOrderByMotorized(String motorizedId, List<OrderTrackerStatus> status) {
        List<OrderTracker> orders = ordertrackerRepository.findByMotorized_IdAndOrderTrackerStatusInOrderByGroupOrderAsc(motorizedId, status);
        return orders.stream().map(OrderTrackerMapper::mapOrderTrackerToDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderTracker> unassignOrders(String groupName, List<Long> orders) {
        ordertrackerRepository.removeGroupReferences(orders);
        return ordertrackerRepository.findAllByGroupNameOrderByGroupOrderAsc(groupName);
    }

    @Override
    public List<OrderDto> getCurrentOrdersByGroupName(String groupName) {
        List<OrderTracker> orders = orderRepository.findAllByGroupNameOrderByGroupOrderAsc(groupName);
        return orders.stream().map(OrderTrackerMapper::mapOrderTrackerToDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderTracker> getUnassignedOrdersByIds(List<Long> orders) {
        return ordertrackerRepository.findOrdersUnassignedByIds(orders);
    }

    @Override
    public void bulkUpdateOrderStatus(List<Long> orderIds, OrderTrackerStatus.Code status) {    	
    	LocalDateTime now = LocalDateTime.now();
    	ordertrackerRepository.bulkUpdateStatus(orderIds, status.name(), now);
    }

    @Override
    public List<OrderTracker> getAllOrdersByEcommercePurchaseId(List<Long> orderIds) {
        return ordertrackerRepository.findAllByEcommercePurchaseIdIn(orderIds);
    }

    @Override
    public OrderTracker findOrderTrackerByOrderTrackingCode(String orderTrackingCode) {
        return ordertrackerRepository.findOrderTRackerByOrderTrackingCode(orderTrackingCode);
    }

    @Override
    public PageDto<OrderDto> findOrdersByLocal(String localCode, Integer pageNumber, String orderId) {
    	OrderTrackerStatus status = new OrderTrackerStatus();
    	status.setCode(OrderTrackerStatus.Code.UNASSIGNED);
    	Integer pageSize = parameterService.parameterByCode(Constant.SEARCH_ORDER_PAGE_SIZE).getIntValue();
    	
    	if (!StringUtils.isEmpty(orderId)) {
    		return new PageDto<>(ordertrackerRepository.findByLocalCodeAndOrderTrackerStatusAndOrderId(
    				localCode, status, false, orderId, PageRequest.of(pageNumber, pageSize))
        			.map(OrderTrackerMapper::mapOrderTrackerToDtoForMotorized));
    	} else 
    	{
    		return new PageDto<>(ordertrackerRepository.findByLocalCodeAndOrderTrackerStatusAndExternalRouting(localCode, status, false, PageRequest.of(pageNumber, pageSize))
        			.map(OrderTrackerMapper::mapOrderTrackerToDtoForMotorized));
    	}
    }

    @Override
    public void validateTravel(TravelDto travelDto, String motorizedId) {
    	if(StringUtils.isEmpty(travelDto.getLocalCode())){
    		log.error("Request dont has localCode");
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
    	}

        if(travelDto.getOrders().isEmpty()){
            log.error("Request dont has orders");
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
        }

        MotorizedType motorizedType = userRepository.findMotorizedType(motorizedId);
        if (motorizedType == null || motorizedType.getServices() == null) {
            log.error("The motorized with id {} does not have a type assigned", motorizedId);
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
        }

        List<DeliveryTravel.TravelStatus> statusList = Arrays.asList(DeliveryTravel.TravelStatus.CREATED, DeliveryTravel.TravelStatus.IN_PROGRESS);
        Long activeTravelsCount = deliveryTravelRepository.countByMotorizedIdAndTravelStatusIn(motorizedId, statusList);
        if (activeTravelsCount > NumberUtils.LONG_ZERO) {
            log.error("The motorized with id {} has active travels", motorizedId);
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
        }

        List<String> orderTrackingCodes = travelDto.getOrders();
        List<OrderHeaderProjection> orders = ordertrackerRepository.findOrdersToAssign(orderTrackingCodes);

        log.info("validating motorized {} with type {}", motorizedId, motorizedType.getCode().name());

        orders.forEach(order -> {
            log.info("validating order {} with status {} and service type {}"
                    , order.getEcommerceId(), order.getOrderStatus(), order.getServiceType());

            if (!OrderTrackerStatus.Code.UNASSIGNED.name().equals(order.getOrderStatus())) {
                log.error("The order with id {} is not assignable, should be in UNASSIGNED status", order.getEcommerceId());
                throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
            }
            if (order.getMotorizedId() != null) {
                log.error("Cannot create travel due the order with id {} is already assigned", order.getEcommerceId());
                throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
            }

            List<Constant.ServiceType> codes = motorizedType.getServices().stream().map(ServiceType::getCode).collect(Collectors.toList());
            Constant.ServiceType serviceType = Constant.ServiceType.parseByName(order.getServiceType());

            if (!codes.contains(serviceType)) {
                log.error("Order with id {} and service type {} cannot be assigned to the motorized {}"
                        , order.getEcommerceId(), order.getServiceType(), motorizedId);
                throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
            }
        });
    }

    @Override
    public void cancelTravel(DeliveryTravel deliveryTravel) {
        List<Long> orderIds = new ArrayList<>();

        deliveryTravel.getDetailList().forEach(detailTravel -> {
            OrderTracker order = detailTravel.getOrderTracker();
            if(!OrderTrackerStatus.Code.FINALIZED.contains(order.getOrderTrackerStatus().getCode()))
            {
                order.setMotorized(null);
                order.setGroupName(null);
                order.setGroupOrder(null);
                order.setInGroupProcess(null);


                ordertrackerRepository.save(order);

                orderIds.add(order.getId());
            }
        });

        log.info("Delivery travel for orders {} has been cancelled", StringUtils.join(orderIds));
    }
    
    @Override
    public void removeGroupReferences(List<Long> ordersIds) {
        ordertrackerRepository.removeGroupReferences(ordersIds);
    }
    
    @Override
	public OrderUpdateDto updateOrderHistory(String orderTrackingCode, String motorizedId, List<OrderStatusDto> orderStatusHistoryDto) {
    	Optional<OrderHeaderProjection> opt = ordertrackerRepository.findOrderHeaderByTrackingCode(orderTrackingCode);
    	return opt.map(header -> {
    		
    		OrderTrackerStatus.Code code = OrderTrackerStatus.Code.parse(header.getOrderStatus());
    	
	    	if (OrderTrackerStatus.Code.FINALIZED.contains(code)) {
	    		log.error("Order {} finalized, cannot get a new status", header.getEcommerceId());
	    		throw new OrderFinishedException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);   
	    	}
	    	
	    	if (OrderTrackerStatus.Code.UNASSIGNED.equals(code)) {
	    		log.error("Order {} unassinged, cannot get a new status", header.getEcommerceId());
	    		throw new OrderFinishedException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);  
	    	}
	    	
	    	orderStatusHistoryDto.stream().findFirst().ifPresent(firstStatus -> {
	    		OrderTrackerStatus.Code firstCode = OrderTrackerStatus.Code.parse(firstStatus.getStatus());
	    		if (firstCode.equals(code)) {
	    			log.error("Order {} already has the status {}", header.getEcommerceId(), firstCode.name());
	        		throw new OrderFinishedException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR); 
	    		}
	    	});    	
	 
	    	OrderUpdateDto response = new OrderUpdateDto();
	    	response.setCurrentType(Constant.MotorizedType.parseByName(header.getMotorizedType()));
	    	response.setGroupName(header.getGroupName());
	    	response.setOrderId(header.getEcommerceId());
	    	response.setPreviousStatus(header.getOrderStatus());
	    	
	    	if (!CollectionUtils.isEmpty(orderStatusHistoryDto)) {    		
	    		OrderStatusDto lastHistory = CollectionUtils.lastElement(orderStatusHistoryDto);    

	    		LocalDateTime date = DateUtil.getDateTimeFromMillis(lastHistory.getCreationDate());
	    		ordertrackerRepository.updateStatus(header.getEcommerceId(), lastHistory.getStatus(), date);
	    		
	    	} else {
	    		throw new OrderException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);  
	    	}
	    	
	    	return response;
    	}).orElseThrow(() -> new OrderException(Constant.Error.NOT_ORDER, Constant.ErrorCode.ORDER_STATUS_ERROR));
	} 
    
    @Override
	public List<OrderCancelledDto> findOrdersHistorical(String motorizedId, List<OrderTrackerStatus> status, LocalDate date) {			
		return ordertrackerRepository.findOrdersHistorical(motorizedId, status, date.atStartOfDay())
				.stream().map(OrderTrackerMapper::mapOrderTrackerToOrderCancelledDto).collect(Collectors.toList());
	}
    
    public List<OrderMotorizedCanonical> findOrderTrackerByMotorizedId(String motorizedId){  	
    	List<OrderMotorizedCanonical> listOrderMotorizedCanonical=new ArrayList<>();
    	 try {   		 
    		 ordertrackerRepository.findEnabledOrderTrackerByMotorizedId(motorizedId)
    		 .forEach(orderDto->{
    			 OrderMotorizedCanonical orderMotorizedCanonical = OrderMotorizedCanonical.builder()
    					 .orderNumber(orderDto.getOrderNumber())
    					 .motorizedId(orderDto.getMotorizedId())
    					 .orderStatus(orderDto.getOrderStatus())
    					 .build();  			 
    			 listOrderMotorizedCanonical.add(orderMotorizedCanonical);
    		 });   		     		
         } catch (Exception e) {
             log.error("Error in findOrderTrackerByMotorizedId.", e);
             throw new OrderException(Constant.MESSAGE_ERROR, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
         }
    	 return listOrderMotorizedCanonical;
    }
    
    public List<MotorizedCanonical> findMotorizedByLocalCode(Integer localCode){
    	List<MotorizedCanonical> listMotorizedCanonical=new ArrayList<>();
    	try {   		 
	   		 ordertrackerRepository.findMotorizedByLocalCode(localCode)
	   		 .forEach(motorizedDto->{
	   			MotorizedCanonical motorizedCanonical = MotorizedCanonical.builder()
	   					 .motorizedId(motorizedDto.getMotorizedId())
	   					 .status(motorizedDto.getStatus())
	   					 .build();	   			 
	   			listMotorizedCanonical.add(motorizedCanonical);
	   		 });   		   		 
        } catch (Exception e) {
            log.error("Error in findMotorizedByLocalCode.", e);
            throw new OrderException(Constant.MESSAGE_ERROR, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    	return listMotorizedCanonical;
    }

	@Override
	public OrderDetailCanonical findOrderTrackerByEcommerceId(Long orderNumber) {
        OrderDetailCanonical orderDetailCanonical = null;
        try {
            Optional<OrderTrackerDetailDto> optionalOrderTracker = Optional.ofNullable(ordertrackerRepository.findOrderTrackerByEcommerceId(orderNumber));
            if (optionalOrderTracker.isPresent()) {
                OrderFulfillmentDto orderFulfillmentDto = externalService.getOrderNumber(orderNumber);
                OrderTrackerDetailDto orderTracker=optionalOrderTracker.get();                
                BigDecimal diff = redondeoTotalCost(orderTracker.getTotalAmount());
                BigDecimal paymentAmount = redondeTotalAmount(Util.formatNumberBigDecimal(orderTracker.getPaymentAmount()));
                BigDecimal totalAmount = redondeTotalAmount(Util.formatNumberBigDecimal(orderTracker.getTotalAmount()));
                orderDetailCanonical = OrderDetailCanonical.builder()
                        .orderNumber(orderTracker.getOrderNumber())
                        .difPayment(diff)
                        .paymentAmount(paymentAmount)
                        .paymentMethod(orderFulfillmentDto.getPaymentMethodId())
                        .creditCard(orderFulfillmentDto.getCreditCardId())
                        .currency(orderFulfillmentDto.getCurrency())
                        .totalAmount(Util.formatNumberBigDecimal(orderTracker.getTotalAmount()))
                        .scheduledOrderDate((Status.Code.DELIVERED.name().equals(orderTracker.getOrderStatus()))
                                ? DateUtil.getDateTimeFormatted(orderTracker.getScheduledOrderDate())
                                : null)
                        .payOrderDate((PaymentMethod.PaymentType.ONLINE_PAYMENT.name().equals(orderTracker.getPaymentMethod()))
                                ? DateUtil.convertStringDateToIsoFormatDate(orderFulfillmentDto.getConfirmedOrder())
                                : (Status.Code.DELIVERED.name().equals(orderTracker.getOrderStatus()))
                                    ? DateUtil.getDateTimeFormatted(orderTracker.getScheduledOrderDate())
                                    : null)
                        .transactionOrderDate(DateUtil.convertStringDateToIsoFormatDate(orderFulfillmentDto.getConfirmedOrder()))
                        .changeAmount(
                        			Util.formatNumberBigDecimal(orderFulfillmentDto.getPaymentMethodId()==null || orderFulfillmentDto.getPaymentMethodId()!=1?new BigDecimal(0):redondeChangeAmount(paymentAmount, totalAmount))
                        	                   	
                         )
                        .purchaseNumber("")
                        .posCode("")
                        .orderStatus(orderTracker.getOrderStatus())
                        .motorizedId(orderTracker.getMotorizedId())
                        .travel(orderTracker.getTravel())
                        .build();
            } else {
            	OrderFulfillmentDto orderFulfillmentDto = externalService.getOrderNumber(orderNumber);
            	if(orderFulfillmentDto != null && orderFulfillmentDto.getOrderStatus() != null) {
	            	log.info("Order Status from Fullfilment: "+orderFulfillmentDto.getOrderStatus());
	            	orderDetailCanonical = OrderDetailCanonical.builder()
	            			.orderStatus(Util.getOrderStatus(orderFulfillmentDto.getOrderStatus()))
	            			.build();
            	}else {
            		log.error("Error in findOrderTrackerByEcommerceId - no tada found");                	
                	orderDetailCanonical = OrderDetailCanonical.builder()
	            			.orderStatus(Constant.MESSAGE_NO_DATA)
	            			.build();
            	}
            }
        } catch (Exception e) {         	
            log.error("Error in findOrderTrackerByEcommerceId.", e);
            throw new OrderException(Constant.MESSAGE_ERROR, Constant.ErrorCode.DEFAULT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return orderDetailCanonical;
    }
	
	public BigDecimal redondeoTotalCost(BigDecimal total) {
		total = total.setScale(2, RoundingMode.HALF_UP);
		BigDecimal result  = total.setScale(1, RoundingMode.HALF_UP);
		if(result.compareTo(total) > 0) {
			result = result.subtract(BigDecimal.valueOf(0.1));
		}else if(result.compareTo(total)==0) {
			return BigDecimal.ZERO;
		}
		result = total.subtract(result);
		result = result.setScale(2, RoundingMode.HALF_UP);
		return result;
	}
	
	public BigDecimal redondeTotalAmount(BigDecimal amount) {
		BigDecimal result = BigDecimal.ZERO;
		if(amount != null) {
		amount = amount.setScale(1, RoundingMode.DOWN);
		result = amount.setScale(2);
		}else {
			result = new BigDecimal(0);
		}
		return result;		
	}
	
	public BigDecimal redondeChangeAmount(BigDecimal amount, BigDecimal totalAmount) {
		BigDecimal result = BigDecimal.ZERO;
		if(amount != null && amount.compareTo(result) > 0) {		
		result = amount.subtract(totalAmount);
		}else {
			result = new BigDecimal(0);
		}
		return result;		
	}

	@Override
	public Optional<OrderHeaderProjection> findOrderHeaderByEcommerceId(Long ecommerceId) {
		return ordertrackerRepository.findOrderHeaderByEcommerceId(ecommerceId);
	}

	@Override
	public Optional<OrderHeaderProjection> findOrderHeaderByTrackingCode(String trackingCode) {
		return ordertrackerRepository.findOrderHeaderByTrackingCode(trackingCode);
	}

    @Override
    public boolean updatePartialOrderHeader(PartialOrderDto orderDto) {
        BigDecimal totalCost = orderDto.getTotalCost();
        BigDecimal deliveryCost = orderDto.getDeliveryCost();
        /*validar fechas*/
        LocalDateTime dateLastUpdated =  LocalDateTime.now();
        Long externalPurchaseId = orderDto.getEcommercePurchaseId();

        orderRepository.updatePartialOrder(totalCost,deliveryCost,dateLastUpdated,externalPurchaseId,true);
        log.info("The order {} header was updated sucessfully",externalPurchaseId);
        return true;
    }

    @Override
    public boolean updatePartialOrderDetail(PartialOrderDto orderDto, List<IOrdertrackerItem> iOrderTrackerItem) {
        log.info("start updatePartialOrderDetail");
        for (IOrdertrackerItem itemOriginal : iOrderTrackerItem) {
            OrderItemDto itemDto = orderDto.getOrderItem().stream().filter(dto-> !dto.isRemoved()) .filter(dto -> dto.getProductCode()
                    .equals(itemOriginal.getProductCode())).findFirst().orElse(null);
            if (itemDto == null) {
                log.info("The item {} of the order {} is removed because it does not exist in the list to update",
                        itemOriginal.getProductCode(),orderDto.getEcommercePurchaseId());

                deleteItemRetired(itemOriginal.getProductCode(), iOrderTrackerItem.get(0).getOrderTrackerId());
            } else {
                if(itemDto.isEdited()){
                    Long orderTrackerId = itemOriginal.getOrderTrackerId();
                    String productCode = itemOriginal.getProductCode();
                    Integer quantity = itemDto.getQuantity();
                    BigDecimal unitPrice = itemDto.getUnitPrice();
                    BigDecimal totalPrice = itemDto.getTotalPrice();

                    Constant.Logical fractionated = Constant.Logical.parse(itemDto.getFractionated());
                    orderRepository.updateItemsPartialOrder(quantity, unitPrice, totalPrice, fractionated.name(),
                            orderTrackerId, productCode);
                }
            }
        }
        return true;
    }

    @Override
    public boolean deleteItemRetired(String itemId, Long orderId) {
        log.info("Deleting itemId: {} from orderId: {}",itemId,orderId);
        orderRepository.deleteItemRetired(itemId,orderId);

        return true;
    }

    @Override
    public void updatePaymentMethod(PartialOrderDto partialOrderDto, Long orderFulfillmentId) {
        PaymentMethodDto paymentMethod = partialOrderDto.getPayment();
        BigDecimal paidAmount = paymentMethod.getPaidAmount();
        BigDecimal changeAmount = paymentMethod.getChangeAmount();
        orderRepository.updatePaymentMethod(paidAmount,changeAmount,"Parcial",orderFulfillmentId);
        log.info("PaymentMethod updated succesfully");
    }

    @Override
    public List<IOrdertrackerItem> getOrderItemByOrderTrackerId(Long orderTrackerId) {
        return orderRepository.getOrderItemByOrderTrackerId(orderTrackerId);
    }

    @Override
    public List<OrderDto> listOrderInfoMotorized(List<Long> ecommerceId) {
        List<OrderTracker> list=orderRepository.findOrderTrackerMotorized(ecommerceId);
        if(list!=null){
            List<OrderDto> ordersDTO = list.stream().parallel().map(item -> {
                OrderDto orderDto=OrderTrackerMapper.mapOrderTrackerToDto(item);
                return orderDto;
            }).collect(Collectors.toList());
            return ordersDTO;
        }
        return null;
    }

    @Override
    public IOrderTracker getOrderByecommerceId(Long ecommerceId) {
        log.info("ecommerceId:{}",ecommerceId);
        return orderRepository.getOrderByecommerceId(ecommerceId).stream().findFirst().orElse(null);
    }
}
