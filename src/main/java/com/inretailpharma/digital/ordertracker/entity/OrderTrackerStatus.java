package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inretailpharma.digital.ordertracker.exception.OrderException;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.EnumUtils;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "order_tracker_status")
public class OrderTrackerStatus implements Serializable {
	
	@Id
    @Enumerated(EnumType.STRING)
    private Code code;
    private String description;

    public enum Code {
    	CANCELLED("11", "10", "CANCEL_ORDER", true),
    	REJECTED("34", "10", "REJECT_ORDER", false),
    	DELIVERED("12", "09", "DELIVER_ORDER", true),
    	
    	ASSIGNED("17", "06", "ASSIGN_ORDER", false),
    	EXT_ASSIGNED("64", "65", "ASSIGN_ORDER", false),
    	PREPARED("19", "05", "PREPARE_ORDER", false),
    	ON_ROUTE("20", "07", "ON_ROUTE_ORDER", false),
    	ARRIVED("21", "08", "ARRIVAL_ORDER", false),
    	   	
    	UNASSIGNED("19", "05", "PREPARE_ORDER", false),
    	ON_HOLD("70","71","ON_HOLD_ORDER", false),
    	ERROR_UPDATE("36", "36", "ERROR_UPDATE", false),
    	INVALID("-1", "-1", "NONE", false),

        FINALIZED(DELIVERED, REJECTED, CANCELLED),
        NOT_IN_TRAVEL(DELIVERED, REJECTED, CANCELLED, UNASSIGNED),
        DISPATCHED(ARRIVED, ASSIGNED, EXT_ASSIGNED, ON_HOLD, ON_ROUTE),
        DISPATCHED_FINALIZED(ARRIVED, ASSIGNED, EXT_ASSIGNED, ON_HOLD, ON_ROUTE, CANCELLED, REJECTED, DELIVERED),
    	DISCARDED(REJECTED, CANCELLED),
    	INTERNAL(ON_HOLD);

    	private String value;
    	private String errorValue;
        private String action;        
        private List<Code> children;
        private boolean messagingEnabled;
        
        private Code(String value, String errorValue, String action, boolean messagingEnabled) {
        	this.value = value;
            this.errorValue = errorValue;
        	this.action = action;
        	this.messagingEnabled = messagingEnabled;
        }

        private Code(Code... children) {
            this.children = Arrays.asList(children);
            this.action = "NONE";
            this.messagingEnabled = false;
        } 

        public static Code parse(String status) {
            for (Code code : values()) {
                if (code.name().equals(status)) {
                    return code;
                }
            }
            throw new OrderException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);  
        }
        
        public static Code parseByAction(String action) {
        	return EnumUtils.getEnumList(Code.class).stream()
                    .filter(item -> item.action.equalsIgnoreCase(action)).findFirst().orElse(INVALID);        	
        }

        public boolean contains(Code code) {
        	if (this.children != null) {
        		return this.children.contains(code);
        	} else {
        		return this.equals(code);
        	}
        }
        
        public boolean contains(String codeName) {
        	return contains(Code.parse(codeName));
        }
        
        public String getValue() {
        	return this.value;
        }
        
        public String getAction() {
        	return this.action;
        }
        
        public String getErrorValue() {
        	return this.errorValue;
        }
        
        public List<Code> getChildren() {
        	if (this.children != null) {
        		return this.children;
        	}
        	return Collections.emptyList();
        }
        
        public List<String> getChildrenNames() {
        	if (this.children != null) {
        		return this.children.stream().map(Code::name)
        				.collect(Collectors.toList());
        	}
        	return Collections.emptyList();
        }
        
        public boolean isMessagingEnabled() {
        	return this.messagingEnabled;
        }
    }
}
