package com.inretailpharma.digital.ordertracker.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.canonical.tracker.*;
import com.inretailpharma.digital.ordertracker.entity.projection.IOrderTracker;
import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.entity.custom.Scheduled;
import com.inretailpharma.digital.ordertracker.entity.custom.Shelf;
import com.inretailpharma.digital.ordertracker.repository.ClientRepository;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ObjectToMapper {
    @Autowired
    private ClientRepository clientRepository;

    public OrderTracker convertOrderDtoToOrderTrackerEntity(OrderDto orderDto) {
        log.info("[START] map-convertOrderDtoToOrderTrackerEntity");
        OrderTracker orderTracker = new OrderTracker();

        orderTracker.setOrderTrackingCode(Util.generateTrackingCode());
        orderTracker.setEcommercePurchaseId(orderDto.getEcommerceId());
        orderTracker.setExternalPurchaseId(orderDto.getExternalId());
        orderTracker.setBridgePurchaseId(orderDto.getBridgePurchaseId());

        OrderTrackerStatus trackerStatus = new OrderTrackerStatus();

        if (orderDto.getOrderStatus() != null) {
            trackerStatus.setCode(OrderTrackerStatus.Code.valueOf(orderDto.getOrderStatus().getCode()));
        } else {
            trackerStatus.setCode(OrderTrackerStatus.Code.UNASSIGNED);
        }

        orderTracker.setOrderTrackerStatus(trackerStatus);

        Scheduled schedule = new Scheduled();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime statusDateTime = LocalDateTime.parse(orderDto.getOrderDetail().getConfirmedSchedule(), formatter);
        orderTracker.setOrderStatusDate(statusDateTime); // falta enviar
        orderTracker.setTotalCost(orderDto.getTotalAmount());
        orderTracker.setDeliveryCost(orderDto.getDeliveryCost());

        String documentNumber = StringUtils.trim(orderDto.getClient().getDocumentNumber());
        Client client = null;

        if (StringUtils.isNotBlank(documentNumber) && !Constant.INVALID_CLIENT_DOCUMENTS.contains(documentNumber)) {
        	client = clientRepository.findFirstByDniOrderByIdDesc(documentNumber);
        } else {
            String phoneNumber = StringUtils.trim(orderDto.getClient().getPhone());
            log.info("convertOrderDtoToOrderTrackerEntity - order {} with invalid document number {}, using phone number as identifier {}"
                    , orderDto.getEcommerceId(), documentNumber, phoneNumber);
            if (StringUtils.isNotBlank(phoneNumber)) {
                client = clientRepository.findFirstByPhoneOrderByIdDesc(phoneNumber);
            }
        }

        if (null != client) {
            client.setFirstName(orderDto.getClient().getFullName());
            client.setEmail(orderDto.getClient().getEmail());
            client.setPhone(orderDto.getClient().getPhone());
        } else {
            client = new Client();
            client.setDni(orderDto.getClient().getDocumentNumber());
            client.setFirstName(orderDto.getClient().getFullName());
            client.setEmail(orderDto.getClient().getEmail());
            client.setPhone(orderDto.getClient().getPhone());
            client.setInkaClubClient((null != orderDto.getClient().getHasInkaClub() && orderDto.getClient().getHasInkaClub() == 0) ? "S" : "N");
        }
        orderTracker.setClient(client);
        
        if (orderDto.getOrderDetail().getConfirmedSchedule() != null) {
            LocalDateTime startDateTime = LocalDateTime.parse(orderDto.getOrderDetail().getConfirmedSchedule(), formatter);
            schedule.setStartDate(startDateTime);
            
            Integer leadTime = (orderDto.getOrderDetail().getLeadTime() != null) 
            		? orderDto.getOrderDetail().getLeadTime() 
            		: Constant.DEFAULT_LEAD_TIME;
            		
            schedule.setEndDate(startDateTime.plusMinutes(leadTime));
        }

        orderTracker.setScheduled(schedule);

        AddressTracker addressTracker = convertOrderDtoToAddressTrackerEntity(orderDto);
        orderTracker.setAddressTracker(addressTracker);

        OrderTrackerDetail orderTrackerDetail = new OrderTrackerDetail();

        orderTrackerDetail.setServiceTypeCode(orderDto.getOrderDetail().getServiceCode());
        orderTrackerDetail.setServiceTypeDescription(orderDto.getOrderDetail().getServiceName());
        orderTrackerDetail.setCompanyDescription(orderDto.getCompany());
        orderTrackerDetail.setCompanyCode(orderDto.getCompanyCode());
        orderTrackerDetail.setCenterCode(orderDto.getLocalCode());
        orderTrackerDetail.setCenterDescription(orderDto.getLocal());
        orderTracker.setOrderTrackerDetail(orderTrackerDetail);

        ReceiptType receiptType = new ReceiptType();
        receiptType.setDni(orderDto.getClient().getDocumentNumber());
        receiptType.setName(orderDto.getReceipt().getType());
        receiptType.setCompanyName(orderDto.getReceipt().getCompanyName());
        receiptType.setCompanyAddress(orderDto.getReceipt().getAddress());
        receiptType.setRuc(orderDto.getReceipt().getRuc());
        receiptType.setReceiptNote(orderDto.getReceipt().getNote());
        orderTracker.setReceiptType(receiptType);

        orderTracker.setOrderItemList(
                orderDto.getOrderItems().stream().map(item -> {
                    OrderTrackerItem orderTrackerItem = new OrderTrackerItem();
                    orderTrackerItem.setProductCode(item.getProductCode());
                    orderTrackerItem.setName(item.getProductName());
                    orderTrackerItem.setProductSku(item.getProductCode());
                    orderTrackerItem.setShortDescription(item.getShortDescription());
                    orderTrackerItem.setBrand(item.getBrand());
                    orderTrackerItem.setQuantity(item.getQuantity());
                    orderTrackerItem.setUnitPrice(item.getUnitPrice());
                    orderTrackerItem.setTotalPrice(item.getTotalPrice());
                    orderTrackerItem.setFractionated(Constant.Logical.parse(item.getFractionated()));
                    return orderTrackerItem;
                }).collect(Collectors.toList())
        );

        orderTracker.setPaymentMethod(paymentMethodConverter(orderDto.getPaymentMethod()));
        
        Optional.ofNullable(orderDto.getShelfList()).ifPresent(shelfList -> 
            orderTracker.setShelfList(
                    shelfList.stream().map(shelfCanonical -> {
                        Shelf shelf = new Shelf();
                        shelf.setLockCode(shelfCanonical.getLockCode());
                        return shelf;
                    }).collect(Collectors.toList())
            )
        );

        orderTracker.setPayBackEnvelope(orderDto.getPayBackEnvelope());

        orderTracker.setDrugstoreId(orderDto.getDrugstoreId());
        orderTracker.setLocalCode(orderDto.getLocalCode());
        orderTracker.setExternalRouting(orderDto.isExternalRouting());

        return orderTracker;
    }

    public AddressTracker convertOrderDtoToAddressTrackerEntity(OrderDto orderDto) {
        AddressTracker addressTracker = new AddressTracker();

        addressTracker.setStreet(orderDto.getAddress().getName());
        addressTracker.setDepartment(orderDto.getAddress().getDepartment());
        addressTracker.setProvince(orderDto.getAddress().getProvince());
        addressTracker.setDistrict(orderDto.getAddress().getDistrict());
        addressTracker.setLatitude(orderDto.getAddress().getLatitude());
        addressTracker.setLongitude(orderDto.getAddress().getLongitude());
        addressTracker.setNotes(orderDto.getAddress().getNotes());
        addressTracker.setApartment(orderDto.getAddress().getApartment());
        addressTracker.setNumber(orderDto.getAddress().getNumber());
        return addressTracker;
    }

    public OrderTrackerDetail convertOrderDtoToOrderTrackerDetailEntity(OrderDto orderDto) {

        OrderTrackerDetail orderTrackerDetail = new OrderTrackerDetail();
        orderTrackerDetail.setServiceTypeCode(orderDto.getOrderDetail().getServiceCode());
        orderTrackerDetail.setServiceTypeDescription(orderDto.getOrderDetail().getServiceName());
        orderTrackerDetail.setCompanyCode(orderDto.getCompany());
        orderTrackerDetail.setCenterCode(orderDto.getLocalCode());
        orderTrackerDetail.setCenterDescription(orderDto.getLocal());

        return orderTrackerDetail;
    }
    
    public TrackerReasonDto convertCancellationReasonDtoToTrackerReasonDto(CancellationReasonDto cancellationReasonDto) {
    	TrackerReasonDto trackerReasonDto = new TrackerReasonDto();
    	trackerReasonDto.setId(cancellationReasonDto.getCode());
    	trackerReasonDto.setReason(cancellationReasonDto.getDescription());
    	return trackerReasonDto;
    }

    private PaymentMethod paymentMethodConverter(PaymentMethodDto payment) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentType(PaymentMethod.PaymentType.getPaymentTypeByNameType(payment.getType()));
        if (null != payment.getCardProvider()) {
            paymentMethod.setCardProvider(payment.getCardProvider());
        }
        if (null != payment.getPaidAmount()) {
            paymentMethod.setPaidAmount(payment.getPaidAmount());
        }
        if (null != payment.getChangeAmount()) {
            paymentMethod.setChangeAmount(payment.getChangeAmount());
        }
        paymentMethod.setDescription("");
        return paymentMethod;
    }

    public OrderCanonical convertIOrderDtoToOrderTrackerCanonical(IOrderTracker iOrderTracker) {
        log.debug("[START] map-convertIOrderDtoToOrderTrackerCanonical");

        OrderCanonical orderCanonical = new OrderCanonical();
        Optional.ofNullable(iOrderTracker).ifPresent(o -> {

            orderCanonical.setId(iOrderTracker.getOrderTrackerId());
            orderCanonical.setEcommercePurchaseId(o.getEcommercePurchaseId());
            orderCanonical.setOrderExternalId(o.getExternalPurchaseId());
            //orderCanonical.setPurchaseId(Optional.ofNullable(o.getPurchaseId()).orElse(null));

            orderCanonical.setTotalAmount(o.getTotalCost());
            orderCanonical.setDeliveryCost(o.getDeliveryCost());

            //orderCanonical.setCompany(o.getCompanyName());
            orderCanonical.setLocalCode(o.getLocalCode());
            orderCanonical.setLocal(o.getLocal());

            ClientCanonical client = new ClientCanonical();
            client.setDocumentNumber(o.getDocumentNumber());
            client.setFullName(Optional.ofNullable(o.getLastName()).orElse(StringUtils.EMPTY)
                    + StringUtils.SPACE + Optional.ofNullable(o.getFirstName()).orElse(StringUtils.EMPTY));
            client.setEmail(o.getEmail());
            client.setPhone(o.getPhone());

            /*OrderDetailCanonical orderDetail = new OrderDetailCanonical();
            Optional.ofNullable(o.getScheduledTime()).ifPresent(date -> {
                orderDetail.setConfirmedSchedule(DateUtil.getLocalDateTimeWithFormat(date));
            });
            orderDetail.setLeadTime(o.getLeadTime());
            orderDetail.setServiceCode(o.getServiceTypeCode());
            orderDetail.setServiceName(o.getServiceTypeName());
            orderDetail.setServiceType(o.getServiceType());*/

            AddressCanonical address = new AddressCanonical();
            address.setName(Optional.ofNullable(o.getStreet()).orElse(StringUtils.EMPTY));
            address.setNumber(Optional.ofNullable(o.getNumber()).orElse(StringUtils.EMPTY));
            address.setDepartment(o.getDepartment());
            address.setProvince(o.getProvince());
            address.setDistrict(o.getDistrict());
            address.setLatitude(o.getLatitude());
            address.setLongitude(o.getLongitude());
            address.setNotes(o.getNotes());
            address.setApartment(o.getApartment());

            ReceiptCanonical receipt = new ReceiptCanonical();
            receipt.setType(o.getReceiptType());
            receipt.setCompanyName(o.getCompanyNameReceipt());
            receipt.setAddress(o.getCompanyAddressReceipt());
            receipt.setRuc(o.getRuc());
            receipt.setNote(o.getNoteReceipt());

            PaymentMethodCanonical paymentMethod = new PaymentMethodCanonical();
            paymentMethod.setType(o.getPaymentType());
            paymentMethod.setCardProvider(o.getCardProvider());
            paymentMethod.setPaidAmount(o.getPaidAmount());
            paymentMethod.setChangeAmount(o.getChangeAmount());

            orderCanonical.setClient(client);
            //orderCanonical.setOrderDetail(orderDetail);
            orderCanonical.setAddress(address);
            orderCanonical.setReceipt(receipt);
            orderCanonical.setPaymentMethod(paymentMethod);

            orderCanonical.setPartial(o.getPartial());

        });

        log.debug("[END] map-convertIOrderDtoToOrderFulfillmentCanonical:{}", orderCanonical);
        return orderCanonical;
    }


    public OrderDistanceAudit convertOrderDistanceAuditTrackerEntity(String uID, OrderDistanceAuditDto orderDistanceAuditDto)
    {

        OrderDistanceAudit orderDistanceAudit = new OrderDistanceAudit();
        orderDistanceAudit.setDistance(String.valueOf(orderDistanceAuditDto.getDistance()));
        orderDistanceAudit.setDeliveryLatitude(orderDistanceAuditDto.getDeliveryLatitude());
        orderDistanceAudit.setDeliveryLongitude(orderDistanceAuditDto.getDeliveryLongitude());
        orderDistanceAudit.setUserLatitude(orderDistanceAuditDto.getUserLatitude());
        orderDistanceAudit.setUserLongitude(orderDistanceAuditDto.getUserLongitude());
        orderDistanceAudit.setUserGps(orderDistanceAuditDto.isUserGps());
        orderDistanceAudit.setTrackingCode(orderDistanceAuditDto.getTrackingCode());
        orderDistanceAudit.setEcommerceId(Long.parseLong(orderDistanceAuditDto.getEcommerceId()));
        orderDistanceAudit.setUser(uID);
        orderDistanceAudit.setCreateDate(LocalDateTime.now());
        return orderDistanceAudit;

    }


}
