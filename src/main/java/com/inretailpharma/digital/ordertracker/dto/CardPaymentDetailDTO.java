package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardPaymentDetailDTO implements Serializable {
    private Long id;
    private String orderId;
    private String codLocal;
    private String transactionResult;
    private String cardBin;
    private String cardBrand;
    private String currency;
    private Float amount;
    private Integer installments;
    private String transactionId;
    private String authorizationCode;
    private String purcherNumber;
    private String transactionDate;
    private String transactionTime;
    private String createdBy;
    private String dateCreated;
    private String lastUpdateBy;
    private String dateLastUpdated;
}


