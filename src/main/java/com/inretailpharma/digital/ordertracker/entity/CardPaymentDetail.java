package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "card_payment_detail")
public class CardPaymentDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "cod_local")
    private String codLocal;

    @Column(name = "transaction_result")
    private String transactionResult;

    @Column(name = "card_bin")
    private String cardBin;

    @Column(name = "card_brand")
    private String cardBrand;

    private String currency;

    private Float amount;

    private Integer installments;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "authorization_code")
    private String authorizationCode;

    @Column(name = "purcher_number")
    private String purcherNumber;

    @Column(name = "transaction_date")
    private String transactionDate;

    @Column(name = "transaction_time")
    private String transactionTime;

    private String createdBy;

    private String dateCreated;

    private String lastUpdateBy;

    private String dateLastUpdated;

}
