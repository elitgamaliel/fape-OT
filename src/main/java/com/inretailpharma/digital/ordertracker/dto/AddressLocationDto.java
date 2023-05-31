package com.inretailpharma.digital.ordertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class AddressLocationDto implements Serializable {
    private BigDecimal latitude;
    private BigDecimal longitude;

}
