package com.inretailpharma.digital.ordertracker.canonical.tracker;

import lombok.Data;

@Data
public class ReceiptCanonical {
    private String type;
    private String ruc;
    private String companyName;
    private String address;
    private String note;
}
