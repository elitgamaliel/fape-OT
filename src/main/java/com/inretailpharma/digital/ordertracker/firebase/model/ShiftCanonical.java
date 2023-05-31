package com.inretailpharma.digital.ordertracker.firebase.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShiftCanonical implements Serializable {

    private Integer id;
    private String code;
    private Integer amountMotorized;
    private String startHour;
    private String endHour;
    private String startBreakHour;
    private String endBreakHour;
}
