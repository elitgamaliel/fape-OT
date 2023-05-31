package com.inretailpharma.digital.ordertracker.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.inretailpharma.digital.ordertracker.dto.MotorizedDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedOrderDto;
import com.inretailpharma.digital.ordertracker.dto.OrderTrackerDetailDto;

import com.inretailpharma.digital.ordertracker.entity.projection.IOrderTracker;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrdertrackerItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.projection.OrderHeaderProjection;
import org.springframework.transaction.annotation.Transactional;


/**
 * OrdertrackerRepository Repositorio para el almacenamiento
 * de {@link OrderTracker}
 *
 * @author
 */
@Repository
public interface OrdertrackerRepository extends JpaRepository<OrderTracker, Long> {

    List<OrderTracker> findByOrderTrackerStatus(OrderTrackerStatus status);

    List<OrderTracker> findByOrderTrackerStatus_Code(String status);

    List<OrderTracker> findByOrderTrackerStatusIn(List<OrderTrackerStatus> codes);

    OrderTracker findByEcommercePurchaseId(Long ecommercePurchaseId);

    @Query(value = "select ord from OrderTracker ord WHERE ord.motorized.id = (:id) AND ord.orderTrackerStatus in (:codes) order by ord.groupOrder asc")
    List<OrderTracker> findByMotorized_IdAndOrderTrackerStatusInOrderByGroupOrderAsc(@Param("id") String id,@Param("codes") List<OrderTrackerStatus> codes);

    @Modifying
    @Query(value = "UPDATE OrderTracker SET groupName = null, groupOrder = null, inGroupProcess = null, motorized = null WHERE ecommerce_purchase_id IN (:ecommercePurchaseId)")
    void removeGroupReferences(@Param("ecommercePurchaseId") Collection<Long> ecommercePurchaseId);

    List<OrderTracker> findAllByGroupNameOrderByGroupOrderAsc(String groupName);

    boolean existsByEcommercePurchaseId(Long ecommercePurchaseId);

    @Query(value = "select count(*) from delivery_travel D " +
            "inner join delivery_travel_detail DD on DD.delivery_travel_id = D.id inner join order_tracker O on O.id = DD.order_id " +
            "where D.id = :travelDeliveryId and O.order_status_code not in (:orderTrackerStatus)", nativeQuery = true)
    Long countByIdTravelDeliveryAndOrderTrackerStatusNotIn(@Param("travelDeliveryId") Long travelDeliveryId, @Param("orderTrackerStatus") Collection<String> orderTrackerStatus);

    OrderTracker findByOrderTrackingCode(String orderTrackingCode);

    @Query(value = "SELECT ot FROM OrderTracker ot WHERE ot.ecommercePurchaseId  in (:ecommercePurchaseId)")
    List<OrderTracker> findOrdersUnassignedByIds(@Param("ecommercePurchaseId") Collection<Long> ecommercePurchaseId);

    OrderTracker findOrderTRackerByOrderTrackingCode(String orderTrackingCode);
    
    Page<OrderTracker> findByLocalCodeAndOrderTrackerStatusAndExternalRouting(String localCode, OrderTrackerStatus status, boolean externalRouting, Pageable pageable);
    
    @Query("SELECT ot FROM OrderTracker ot WHERE ot.localCode = :localCode AND ot.orderTrackerStatus = :orderTrackerStatus AND ot.externalRouting = :externalRouting "
    		+ " AND CAST(ot.ecommercePurchaseId AS string) LIKE %:orderId%")
    Page<OrderTracker> findByLocalCodeAndOrderTrackerStatusAndOrderId(
    		@Param("localCode") String localCode
    		, @Param("orderTrackerStatus") OrderTrackerStatus status
    		, @Param("externalRouting") boolean externalRouting
    		, @Param("orderId") String orderId, Pageable pageable);
    
    List<OrderTracker> findByOrderTrackingCodeIn(List<String> orderTrackingCodes);
    
    @Query(value = "select o.ecommerce_purchase_id as id, o.ecommerce_purchase_id as ecommerceId, o.order_tracking_code as trackingCode, o.order_status_code as orderStatus, d.service_type_code as serviceType, o.motorized_id as motorizedId"
    		+ " from order_tracker o inner join order_tracker_detail d on (o.id = d.order_tracker_id) WHERE o.order_tracking_code in (:orderTrackingCodes)", nativeQuery = true)
    List<OrderHeaderProjection> findOrdersToAssign(@Param("orderTrackingCodes") List<String> orderTrackingCodes);

    @Query(value = " select  " +
            " ot.ecommerce_purchase_id as orderNumber,  " +
            " u.dni as motorizedId,  " +
            " ot.order_status_code as orderStatus " +
            " from  " +
            " order_tracker ot  " +
            " inner join user u on u.id = ot.motorized_id " +
            " where  " +
            " date(ot.scheduled_start_date) = date(CONVERT_TZ(now(), '+05:00', '+00:00'))  " +
            " and u.dni = :dni and u.user_status ='ENABLED'", nativeQuery = true)
    List<MotorizedOrderDto> findEnabledOrderTrackerByMotorizedId(@Param("dni") String dni);
    
    @Query(value = " select " +
            " u.dni as motorizedId, " +
            " u.user_status as status " +
            " from " +
            " user u " +
            " where u.user_status = 'ENABLED' and tracking_status != 'OFFLINE' " +
            " and u.drugstore_id = :drugstoreId", nativeQuery = true)
    List<MotorizedDto> findMotorizedByLocalCode(@Param("drugstoreId") Integer drugstoreId);

    @Query(value = " select " +
               " ot.ecommerce_purchase_id as orderNumber, " +
               " pm.paid_amount as paymentAmount, " +
               " pm.payment_type as paymentMethod, " +
               " pm.card_provider as paymentDescription, " +
               " ot.total_cost as totalAmount, " +
               " pm.change_amount as changeAmount, " +
               " ot.order_status_code as orderStatus, " +
               " u.dni as motorizedId, " +
               " ot.group_name as travel, " +
               " ot.order_status_date as scheduledOrderDate " +
               " from " +
               " order_tracker ot  " +
               " inner join payment_method pm on pm.order_tracker_id = ot.id " +
               " inner join user u on u.id = ot.motorized_id " +
               " where ot.ecommerce_purchase_id = :orderNumber and u.user_status ='ENABLED'", nativeQuery = true)
       OrderTrackerDetailDto findOrderTrackerByEcommerceId(@Param("orderNumber") Long orderNumber);

    @Query(value = "SELECT o FROM OrderTracker o WHERE o.motorized.id = (:motorizedId) AND o.orderTrackerStatus in (:status) AND o.orderStatusDate >= (:date)")
    List<OrderTracker> findOrdersHistorical(@Param("motorizedId") String motorizedId, @Param("status") List<OrderTrackerStatus> status, @Param("date") LocalDateTime date);
    
    
    
    @Query(value = "SELECT o.ecommerce_purchase_id as ecommerceId, o.order_tracking_code as trackingCode, o.order_status_code as orderStatus,"
    		+ " o.motorized_id as motorizedId, o.group_name as groupName, m.motorized_type_code as motorizedType, d.service_type_code as serviceType FROM order_tracker o"
    		+ " INNER JOIN order_tracker_detail d ON (o.id = d.order_tracker_id)"
    		+ " INNER JOIN motorized_service m ON (d.service_type_code = m.service_type_code)"
    		+ " WHERE o.ecommerce_purchase_id = :ecommerceId LIMIT 1", nativeQuery = true)
    Optional<OrderHeaderProjection> findOrderHeaderByEcommerceId(@Param("ecommerceId") Long ecommerceId);
    
    @Query(value = "SELECT o.ecommerce_purchase_id as ecommerceId, o.order_tracking_code as trackingCode, o.order_status_code as orderStatus,"
    		+ " o.motorized_id as motorizedId, o.group_name as groupName, m.motorized_type_code as motorizedType, d.service_type_code as serviceType FROM order_tracker o"
    		+ " INNER JOIN order_tracker_detail d ON (o.id = d.order_tracker_id)"
    		+ " INNER JOIN motorized_service m ON (d.service_type_code = m.service_type_code)"
    		+ " WHERE o.order_tracking_code = :trackingCode LIMIT 1", nativeQuery = true)
    Optional<OrderHeaderProjection> findOrderHeaderByTrackingCode(@Param("trackingCode") String trackingCode);
    
    @Modifying
    @Query(value = "UPDATE order_tracker SET order_status_code = :orderStatusCode, order_status_date = :orderStatusDate WHERE ecommerce_purchase_id = :ecommerceId", nativeQuery = true)
    void updateStatus(@Param("ecommerceId") Long ecommerceId, @Param("orderStatusCode") String orderStatusCode, @Param("orderStatusDate") LocalDateTime orderStatusDate);
    
    @Modifying
    @Query(value = "UPDATE order_tracker SET order_status_code = :orderStatusCode, order_status_date = :orderStatusDate WHERE ecommerce_purchase_id in (:ecommerceId)", nativeQuery = true)
    void bulkUpdateStatus(@Param("ecommerceId") List<Long> orderIds, @Param("orderStatusCode") String orderStatusCode, @Param("orderStatusDate") LocalDateTime orderStatusDate);
    
    List<OrderTracker> findAllByEcommercePurchaseIdIn(List<Long> orderIds);
    /*Williams*/
    @Modifying
    @Transactional
    @Query(value = "Update order_tracker_item " +
            " set quantity = :quantity ," +
            /*"  quantity_presentation = :quantity_presentation ," +*/
            "  unit_Price = :unitPrice ," +
            "  total_Price = :totalPrice ," +
            "  fractionated = :fractionated " +
            /*" quantity_units = :quantityUnits, "+*/
            /*" presentation_description = :presentation_description, "+*/
            /*" presentation_id = :presentation_id, "+*/
            /*" fractional_discount = :fractional_discount, "+*/
            /*" priceList = :priceList, "+*/
            /*" priceAllPaymentMethod = :priceAllPaymentMethod, "+*/
            /*" WithpaymentMethod = :priceWithpaymentMethod, "+*/
            /*" totalPriceList = :totalPriceList, "+ */
            /*" totalPriceAllPaymentMethod = :totalPriceAllPaymentMethod, "+*/
            /*" totalPriceWithpaymentMethod = :totalPriceWithpaymentMethod, "+*/
            /*" promotionalDiscount = :promotionalDiscount "+*/
            " where order_tracker_id = :orderTrackerId " +
            " and product_code = :productCode",
            nativeQuery = true)
    void updateItemsPartialOrder(@Param("quantity") Integer quantity,
                                 @Param("unitPrice") BigDecimal unitPrice,
                                 @Param("totalPrice") BigDecimal totalPrice,
                                 @Param("fractionated") String fractionated,
                                 @Param("orderTrackerId") Long orderTrackerId,
                                 @Param("productCode") String productCode
    );

    @Modifying
    @Transactional
    @Query(value = "Update order_tracker " +
            " set total_cost = :totalCost ," +
            "  delivery_cost = :deliveryCost ," +
            "  date_last_updated = :date_last_updated, " +
            "  partial = :partial " +
            /*"  discount_applied = :discount_applied, " +*/
            /*"  sub_total_cost = :sub_total_cost, " +*/
            /*"  total_cost_no_discount = :total_cost_no_discount, " +*/
            /*"  discountAppliedNoDP = :discountAppliedNoDP, " +*/
            /*"  subTotalWithNoSpecificPaymentMethod = :subTotalWithNoSpecificPaymentMethod, " +*/
            /*"  totalWithNoSpecificPaymentMethod = :totalWithNoSpecificPaymentMethod, " +*/
            /*"  totalWithPaymentMethod = :totalWithPaymentMethod " +*/
            " where ecommerce_purchase_id = :externalPurchaseId",
            nativeQuery = true)
    void updatePartialOrder(@Param("totalCost") BigDecimal unitPrice,
                            @Param("deliveryCost") BigDecimal totalPrice,
                            @Param("date_last_updated") LocalDateTime date_last_updated,
                            @Param("externalPurchaseId") Long externalPurchaseId,
                            @Param("partial") boolean partial);

    @Modifying
    @Transactional
    @Query(value = " update payment_method " +
            " set paid_amount = :paidAmount ," +
            " change_amount = :changeAmount," +
            " payment_note = :paymentNote " +
            " WHERE  order_tracker_id = :orderId",
            nativeQuery = true)
    void updatePaymentMethod(@Param("paidAmount") BigDecimal paidAmount,
                             @Param("changeAmount") BigDecimal changeAmount,
                             @Param("paymentNote") String paymentNote,
                             @Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM order_tracker_item" +
            " WHERE order_tracker_id = :id " +
            " AND  product_code = :productIdsToRemove",
            nativeQuery = true)

    void deleteItemRetired(@Param("productIdsToRemove")String itemId,
                           @Param("id")Long id
    );

    @Query(value = " select o.id as orderId, o.ecommerce_purchase_id as ecommerceId, " +
            " o.external_purchase_id as externalId, " +
            " o.total_cost as totalCost, o.delivery_cost as deliveryCost, " +
            " c.first_name as firstName, c.last_name as lastName, c.email, c.dni as documentNumber, " +
            " c.phone, c.birth_date as birthDate, c.is_anonymous, " +
            " pm.payment_type as paymentType, pm.card_provider as cardProvider, pm.paid_amount as paidAmount, " +
            " pm.change_amount as changeAmount, pm.card_provider as cardProviderId, " +
            " pm.card_provider as cardProviderCode, " +
            " rt.name as receiptType, rt.dni as documentNumberReceipt, " +
            " rt.ruc as ruc, " +
            " rt.company_name as companyNameReceipt, rt.company_address as companyAddressReceipt, " +
            " rt.receipt_note as noteReceipt, " +
            " af.street, af.number, af.apartment, af.country, af.district, af.province, " +
            " af.department, af.notes, af.latitude, af.longitude, o.partial " +
            " from order_tracker o " +
            " inner join client c on c.id = o.client_id " +
            " inner join payment_method pm on pm.order_tracker_id = o.id " +
            " inner join receipt_type rt on rt.order_tracker_id = o.id " +
            " inner join address_tracker af on af.order_tracker_id = o.id " +
            " where o.ecommerce_purchase_id = :ecommerceId " +
            " order by o.id desc limit 1 ",
            nativeQuery = true
    )
    List<IOrderTracker> getOrderByecommerceId(@Param("ecommerceId") Long ecommerceId);

    @Query(value =" select oi.order_tracker_id as orderTrackerId,oi.product_code as productCode, oi.product_sap_code as productSapCode, " +
            " oi.name as nameProduct, oi.short_description as shortDescriptionProduct, oi.brand as brandProduct, oi.quantity, " +
            " oi.unit_price as unitPrice, oi.total_price as totalPrice, oi.fractionated, " +
            " oi.quantity as quantityUnits, " +
            " oi.fractionated as fractionatedPrice, " +
            " oi.product_code as productCodeInkafarma " +
            " from order_tracker_item oi " +
            " where oi.order_tracker_id = :orderTrackerId ",
            nativeQuery = true
    )
    List<IOrdertrackerItem> getOrderItemByOrderTrackerId(@Param("orderTrackerId") Long orderTrackerId);

    @Query("Select ord from OrderTracker ord WHERE ord.ecommercePurchaseId IN (:ecommercePurchaseId)")
    List<OrderTracker> findOrderTrackerMotorized(@Param("ecommercePurchaseId") List<Long> ecommercePurchaseId);
}
