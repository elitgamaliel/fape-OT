package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Embeddable
@Table(name = "address_tracker")
public class AddressTracker {

    @Column(table = "address_tracker", name="district")
    private String district;

    @Column(table = "address_tracker", name="street")
    private String street;

    @Column(table = "address_tracker", name="number")
    private String number;

    @Column(table = "address_tracker", name="province")
    private String province;

    @Column(table = "address_tracker", name="apartment")
    private String apartment;

    @Column(table = "address_tracker", name="department")
    private String department;

    @Column(table = "address_tracker", name="country")
    private String country;

    @Column(table = "address_tracker", name="latitude")
    private BigDecimal latitude;

    @Column(table = "address_tracker", name="longitude")
    private BigDecimal longitude;

    @Column(columnDefinition = "TEXT", table = "address_tracker", name="notes")
    private String notes;

    @Column(table = "address_tracker", name="ubigeo_code")
    private String ubigeoCode;

}
