package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;

import lombok.Data;

@Data
public class GroupCanonical implements FirebaseObject {
	
	private String name;
	private String inProcess;
}
