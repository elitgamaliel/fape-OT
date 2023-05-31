package com.inretailpharma.digital.ordertracker.service.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrderTracker;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrdertrackerItem;
import com.inretailpharma.digital.ordertracker.entity.*;
import org.springframework.data.domain.Pageable;

import com.inretailpharma.digital.ordertracker.canonical.tracker.MotorizedCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderDetailCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderMotorizedCanonical;
import com.inretailpharma.digital.ordertracker.entity.projection.OrderHeaderProjection;

import reactor.core.publisher.Flux;

/**
 * OrderTracker Service, define el caso de uso del API
 *
 * @author
 */
public interface OrdertrackerService {

    void save(OrderTracker orderTracker);

    Flux<OrderDto> findOrders(Pageable pageable, Long drugstore, List<OrderTrackerStatus.Code> status);

    OrderDto findOrderByEcommercePurchaseId(Long orderId);

    OrderHeaderProjection kitStatusUpdate(OrderHeaderProjection header, OrderTrackerStatus.Code status, String motorizedId, LocalDateTime date);

    List<TrackerReason> getAllTrackerReasonByType(TrackerReason.Type type);

    List<OrderDto> findOrderByMotorized(String motorizedId, List<OrderTrackerStatus> status);

    List<OrderTracker> unassignOrders(String groupName, List<Long> orders);

    List<OrderDto> getCurrentOrdersByGroupName(String groupName);

    void bulkUpdateOrderStatus(List<Long> orderIds, OrderTrackerStatus.Code status);

    List<OrderTracker> getAllOrdersByEcommercePurchaseId(List<Long> orderIds);

    OrderDto findOrderTrackingCode(String orderTrackingCode);

    List<OrderTracker> getUnassignedOrdersByIds(List<Long> orders);

    OrderTracker findOrderTrackerByOrderTrackingCode(String orderTrackingCode);

    PageDto<OrderDto> findOrdersByLocal(String localCode, Integer pageNumber, String orderId);
    
    void validateTravel(TravelDto travelDto, String motorizedId);
    
    void cancelTravel(DeliveryTravel deliveryTravel);
    
    void removeGroupReferences(List<Long> ordersIds);
    

    List<OrderMotorizedCanonical> findOrderTrackerByMotorizedId(String motorizedId);
    
    List<MotorizedCanonical> findMotorizedByLocalCode(Integer localCode);
    
    OrderDetailCanonical findOrderTrackerByEcommerceId(Long orderNumber);

    OrderUpdateDto updateOrderHistory(String orderTrackingCode, String motorizedId, List<OrderStatusDto> orderStatusHistoryDto);
    
    List<OrderCancelledDto> findOrdersHistorical(String motorizedId, List<OrderTrackerStatus> status,  LocalDate date);    
    
    Optional<OrderHeaderProjection> findOrderHeaderByEcommerceId(Long ecommerceId);
    
    Optional<OrderHeaderProjection> findOrderHeaderByTrackingCode(String trackingCode);

    boolean updatePartialOrderHeader(PartialOrderDto orderDto);
    boolean updatePartialOrderDetail(PartialOrderDto orderDto, List<IOrdertrackerItem> iOrderTrackerItem);
    boolean deleteItemRetired(String itemId, Long orderId);
    IOrderTracker getOrderByecommerceId(Long ecommerceId);
    void updatePaymentMethod(PartialOrderDto partialOrderDto, Long orderFulfillmentId);
    List<IOrdertrackerItem> getOrderItemByOrderTrackerId(Long orderTrackerId);
    List<OrderDto> listOrderInfoMotorized(List<Long> ecommerceId);
}
