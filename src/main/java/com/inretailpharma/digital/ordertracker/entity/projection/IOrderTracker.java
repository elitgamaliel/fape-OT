package com.inretailpharma.digital.ordertracker.entity.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IOrderTracker {
    Long getOrderId();
    Long getOrderTrackerId();
    Long getEcommercePurchaseId();
    Long getExternalPurchaseId();

    BigDecimal getTotalCost();
    BigDecimal getDeliveryCost();
    String getLocal();
    String getLocalCode();
    String getDocumentNumber();
    String getLastName();
    String getFirstName();
    String getEmail();
    String getPhone();

    LocalDateTime getScheduledTime();
    Integer getLeadTime();

    String getServiceTypeCode();
    String getServiceTypeName();
    String getServiceType();

    String getStreet();
    String getNumber();
    String getProvince();
    String getDistrict();
    Double getLatitude();
    Double getLongitude();
    String getDepartment();
    String getNotes();
    String getApartment();

    String getReceiptType();
    String getDocumentNumberReceipt();
    String getRuc();
    String getCompanyNameReceipt();
    String getCompanyAddressReceipt();
    String getNoteReceipt();

    String getPaymentType();
    String getCardProvider();
    BigDecimal getPaidAmount();
    BigDecimal getChangeAmount();

    Boolean getPartial();




}
