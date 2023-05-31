package com.inretailpharma.digital.ordertracker.firebase.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MotorizedSettingCanonical implements SettingCanonical {
    private Integer distanceToMarkArrive;
    private Integer deliveryDistance;
    private Integer trackingTime;
    private Integer trackingDistance;
    private Integer travelSpeed;
    private Integer customerDelayTime;
    private Integer routeBufferTime;
    private boolean arrivedValidated;
    private DrugstoreCanonical drugstore;
    private Integer type;
    private boolean markArriveValidated;
    private Integer statusAlertTime;
    private Integer warningPercentageEta;
    private Integer cancelAlarmTime;

}
