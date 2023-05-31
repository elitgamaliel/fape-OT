package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;

@Data
public class MotorizedDeviceCanonical implements FirebaseObject {
    private String imei;
    private String phoneNumber;
    private String phoneMark;
    private String phoneModel;
}
