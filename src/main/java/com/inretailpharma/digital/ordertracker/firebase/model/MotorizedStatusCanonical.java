package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MotorizedStatusCanonical implements FirebaseObject {
    private String statusName;
    private Long statusDate;
    private Double latitude;
    private Double longitude;
    private Long endStatusDate;
    private String message;
}
