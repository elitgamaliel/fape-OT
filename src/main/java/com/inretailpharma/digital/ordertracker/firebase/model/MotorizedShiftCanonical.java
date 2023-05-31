package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;

@Data
public class MotorizedShiftCanonical implements FirebaseObject {
    private Integer id;
    private String code;
    private String startHour;
    private String endHour;
    private String startBreakHour;
    private String endBreakHour;
}
