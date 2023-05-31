package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MotorizedCanonical implements FirebaseObject {

    private String id;
    private MotorizedDeviceCanonical device;
    private MotorizedUserCanonical user;
    private MotorizedStatusCanonical status;
    private DrugstoreCanonical drugstore;
    private SettingCanonical setting;
    private MotorizedShiftCanonical shift;
    private Double latitude;
    private Double longitude;
    private String localType;
}
