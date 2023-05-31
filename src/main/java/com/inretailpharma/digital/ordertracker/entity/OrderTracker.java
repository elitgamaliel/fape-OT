package com.inretailpharma.digital.ordertracker.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import com.inretailpharma.digital.ordertracker.entity.core.TrackerEntity;
import com.inretailpharma.digital.ordertracker.entity.custom.Scheduled;
import com.inretailpharma.digital.ordertracker.entity.custom.Shelf;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Clase OrderTracker subdominio order tracker
 *
 * @author
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "order_tracker")
@SecondaryTables({
        @SecondaryTable(name = "address_tracker", pkJoinColumns = @PrimaryKeyJoinColumn(name = "order_tracker_id", referencedColumnName = "id")),
        @SecondaryTable(name = "payment_method", pkJoinColumns = @PrimaryKeyJoinColumn(name = "order_tracker_id", referencedColumnName = "id")),
        @SecondaryTable(name = "receipt_type", pkJoinColumns = @PrimaryKeyJoinColumn(name = "order_tracker_id", referencedColumnName = "id")),
        @SecondaryTable(name = "order_tracker_detail", pkJoinColumns = @PrimaryKeyJoinColumn(name = "order_tracker_id", referencedColumnName = "id"))
})
public class OrderTracker extends TrackerEntity<Long> {

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_order")
    private Integer groupOrder;

    @Column(name = "delivery_manager_id")
    private Long deliveryManagerId;

    @Column(name = "ecommerce_purchase_id")
    private Long ecommercePurchaseId;

    @Column(name = "external_purchase_id")
    private Long externalPurchaseId;

    @Column(name = "bridge_purchase_id")
    private Long bridgePurchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_code")
    private OrderTrackerStatus orderTrackerStatus;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @Column(name = "delivery_cost")
    private BigDecimal deliveryCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "with_transference")
    private String withTransference;

    @Column(name = "in_group_process")
    @Enumerated(EnumType.STRING)
    private Constant.Logical inGroupProcess;

    @Column(name = "order_status_date")
    private LocalDateTime orderStatusDate;

    @Column(name = "order_tracking_code")
    private String orderTrackingCode;

    @Embedded
    private Scheduled scheduled;

    @ManyToOne
    @JoinColumn(name = "motorized_id")
    private User motorized;

    @Column(name = "order_note")
    private String note;

    private String kit;

    @Column(name = "kit_status")
    private String kitStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sent_to_log_fire")
    private Constant.Logical sentToLogFire;

    @Column(name = "log_fire_attempt")
    private Integer logFireAttempt = 0;

    @Column(name = "log_fire_message")
    private String logFireMessage;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "dispatch_in_log_fire")
    private Constant.Logical dispatchInLogFire;
    
    @Column(name = "message_to_dispatch")
    private String messageToDispatch;
    @Enumerated(EnumType.STRING)
    @Column(name = "cancel_in_log_fire")
    private Constant.Logical cancelInLogFire;
    @Enumerated(EnumType.STRING)
    private Constant.Logical liquidated;
    @Column(name = "pay_back_envelope")
    private String payBackEnvelope;
    @Enumerated(EnumType.STRING)
    @Column(name = "envelope_status")
    private Constant.DispatchStatus envelopeStatus;
    
    @ElementCollection
    @CollectionTable(name = "Shelf", joinColumns = @JoinColumn(name = "order_tracker_id"))
    private List<Shelf> shelfList;

    @Embedded
    private AddressTracker addressTracker;

    @Embedded
    private PaymentMethod paymentMethod;

    @Embedded
    private ReceiptType receiptType;
    @Embedded
    private OrderTrackerDetail orderTrackerDetail;

    @ElementCollection
    @CollectionTable(name = "order_tracker_item", joinColumns = @JoinColumn(name = "order_tracker_id"))
    private List<OrderTrackerItem> orderItemList;

    @Column(name = "drugstore_id")
    private Long drugstoreId;
    
    @Column(name = "local_code")
    private String localCode;
    
    @Column(name = "external_routing")
    private boolean externalRouting;
}
