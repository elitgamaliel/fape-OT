package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;

@Data
public class DrugstoreCanonical implements FirebaseObject {
    private Long id;
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer radius;    
    
    private String localCode;
    private String localName;    
}
