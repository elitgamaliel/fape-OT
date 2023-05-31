package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ClientDto implements Serializable {
    private String fullName;
    private String lastName;
    private String email;
    private String documentNumber;
    private String phone;
    private String birthDate;
    private Integer hasInkaClub;
    private Integer anonimous;
}
