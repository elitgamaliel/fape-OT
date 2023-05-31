package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;

import lombok.Data;

@Data
public class PaymentMethodCanonical implements FirebaseObject {
	
	private String paidAmount;
	private String changeAmount;
	private String type;
	private String note;
	private String provider;	
}
