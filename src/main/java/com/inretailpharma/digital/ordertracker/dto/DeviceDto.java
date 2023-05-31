package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeviceDto implements Serializable {
    private String imei;
    private String phoneNumber;
    private String phoneMark;
    private String phoneModel;
}
