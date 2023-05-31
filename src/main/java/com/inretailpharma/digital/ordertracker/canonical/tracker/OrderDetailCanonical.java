package com.inretailpharma.digital.ordertracker.canonical.tracker;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailCanonical implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long orderNumber;
	private Long paymentMethod;
	private String paymentMethodDescription;
	private String currency;
	private BigDecimal paymentAmount; 
	private BigDecimal totalAmount;
	private String scheduledOrderDate;
	private String payOrderDate;
	private String transactionOrderDate;
	private BigDecimal changeAmount;
	private String purchaseNumber;
	private String posCode;
	private String orderStatus;
	private String motorizedId;
	private String travel;
    private Long creditCard;
    private BigDecimal difPayment;

	private String confirmedSchedule;
	private Integer leadTime;
	/**/
	private String serviceCode;
	private String serviceName;
	private String serviceType;


}
