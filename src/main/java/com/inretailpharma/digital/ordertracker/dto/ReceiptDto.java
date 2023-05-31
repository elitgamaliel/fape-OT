package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptDto implements Serializable {

    private String type;
    private String ruc;
    private String companyName;
    private String address;
    private String note;
}
