package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Embeddable
@Table(name="receipt_type")
public class ReceiptType {

    @Column(table = "receipt_type")
    private String name;

    @Column(table = "receipt_type")
    private String dni;

    @Column(table = "receipt_type")
    private String ruc;

    @Column(table = "receipt_type", name="company_name")
    private String companyName;

    @Column(table = "receipt_type", name="company_address")
    private String companyAddress;

    @Column(table = "receipt_type", name="receipt_note")
    private String receiptNote;
}
