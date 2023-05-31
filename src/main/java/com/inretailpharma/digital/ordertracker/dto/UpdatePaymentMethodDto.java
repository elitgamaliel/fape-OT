package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatePaymentMethodDto {
    private String localId;
    private String authorizationCode;
    private String purcherNumber;
    private String transactionCode;
    private String orderNumber;
    private String status;
    private String message;
}

