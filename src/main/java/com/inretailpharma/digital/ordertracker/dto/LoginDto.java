package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

import java.io.Serializable;

import com.inretailpharma.digital.ordertracker.utils.Constant;

@Data
public class LoginDto implements Serializable {
    private String imei;
    private Double latitude;
    private Double longitude;    
    private String phoneNumber;
    private Type type;
    private Constant.MotorizedType currentMotorizedType;

    public enum Type {
        LOGIN, LOGOUT
    }

}
