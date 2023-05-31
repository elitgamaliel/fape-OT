package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class GroupDto implements Serializable {

    private Integer position;
    private Long orderId;
    private EtaDto eta;
    private Long timeRemaining;
    private OrderDto order;
    private AddressLocationDto addressLocationDto;

    public GroupDto(Integer position, Long orderId, String orderTrackingCode) {
        this.position = position;
        this.orderId = orderId;
        this.order = OrderDto.builder()
        		.ecommerceId(orderId)
        		.orderTrackingCode(orderTrackingCode)
        		.build();        
    }
    
    public String getOrderTrackingCode() {
    	if (this.order != null) {
    		return this.order.getOrderTrackingCode();
    	}
    	return null;
    }
    
    public void setOrderTrackingCode(String orderTrackingCode) {
    	if (this.order != null) {
    		this.order.setOrderTrackingCode(orderTrackingCode);
    	}
    }
}
