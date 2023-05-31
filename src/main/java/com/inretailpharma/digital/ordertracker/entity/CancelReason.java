package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cancel_reason")
public class CancelReason {

    @Id
    private String code;
    private String type;
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_code")
    private Company company;
}
