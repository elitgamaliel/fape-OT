package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;

import lombok.Data;

@Data
public class MotorizedTimeCanonical implements FirebaseObject {
    private Integer etaReturn;
    private Integer eta;

}
