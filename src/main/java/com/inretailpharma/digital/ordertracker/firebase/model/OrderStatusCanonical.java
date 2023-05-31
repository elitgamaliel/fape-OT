package com.inretailpharma.digital.ordertracker.firebase.model;


import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("all")
public class OrderStatusCanonical implements FirebaseObject {

    private String statusName;
    private Long statusDate;
    private String statusNote;
    private Double latitude;
    private Double longitude;
    private String updatedBy;
    private String optionCode;
    private String customNote;
    private String code;
    private String payBackEnvelope;
    private String previousStatus;

}
