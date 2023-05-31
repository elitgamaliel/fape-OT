package com.inretailpharma.digital.ordertracker.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {
	
	private Util() {
		
	}

    private static final int STRING_LENGTH = 12;
    private static char[][] hexarray = {{'a', 'z'}, {'0', '9'}};

    public static String generateTrackingCode() {
        return new RandomStringGenerator.Builder()
                .withinRange(hexarray)
                .build()
                .generate(STRING_LENGTH).toUpperCase();
    }
    
    public static BigDecimal formatNumberBigDecimal(BigDecimal number) {
    	BigDecimal formattedNumber=null;
    	try {
    		if(StringUtils.isEmpty(number)) return formattedNumber;    		
    		formattedNumber = new BigDecimal(new DecimalFormat("#.00").format(number));   		
    	}catch(Exception e){
    		log.error("formatNumberBigDecimal {}", e.getMessage());
    	}    	 
		return formattedNumber;    	
    }
    
    public static String getOrderStatus(String status) {
    	String result = "";
    	switch (status) {    	
    	case Constant.DELIVERED_ORDER:
    			result = "DELIVERED";
    			break;
    	case Constant.ASSIGNED:
    		result =  "ASSIGNED";
    		break;
    	case Constant.READY_PICKUP_ORDER:
    	case Constant.RELEASED_ORDER:
    	case Constant.CONFIRMED_ORDER:
    	case Constant.CONFIRMED_TRACKER:
    	case Constant.PICKED_ORDER:
    	case Constant.PREPARED_ORDER:
    	case Constant.ON_ROUTED_ORDER:
    	case Constant.ARRIVED_ORDER:
    	case Constant.CHECKOUT_ORDER:
    	case Constant.ORDER_FAILED:
    	case Constant.INVOICED:
    	case Constant.PARTIAL_UPDATE_ORDER:
    	case Constant.SUCCESS_DELIVERY_SEND:
    	case Constant.ERROR_NOT_DEFINED:
    	case Constant.ERROR_INSERT_TRACKER:
    	case Constant.ERROR_INSERT_INKAVENTA:
    	case Constant.ERROR_RESERVED_ORDER:
    	case Constant.ERROR_PICKED:
    	case Constant.ERROR_READY_FOR_PICKUP:
    	case Constant.ERROR_ASSIGNED:
    	case Constant.ERROR_ON_ROUTED:
    	case Constant.ERROR_ARRIVED:
    	case Constant.ERROR_DELIVERED:
    	case Constant.ERROR_CANCELLED:
    	case Constant.ERROR_UPDATE:
    	case Constant.ERROR_ORDER_CANCELLED_TRACKER:
    	case Constant.ERROR_INVOICED:
    	case Constant.ERROR_CHECKOUT:
    	case Constant.ERROR_PARTIAL_UPDATE:
    		result = "UNASSIGNED";
    		break;
    	case Constant.REJECTED_ORDER:
    	case Constant.REJECTED_ORDER_ONLINE_PAYMENT:
    		result = "REJECTED";
    		break;
    	case Constant.CANCELLED_ORDER:
    	case Constant.CANCELLED_ORDER_NOT_ENOUGH_STOCK:
    	case Constant.CANCELLED_ORDER_ONLINE_PAYMENT:
    	case Constant.CANCELLED_ORDER_ONLINE_PAYMENT_NOT_ENOUGH_STOCK:    	
    		result = "CANCELLED";
    		break;  
    	default:
    		result = "UNASSIGNED";
			break;
    	}
    	
    	return result;
    }
}
