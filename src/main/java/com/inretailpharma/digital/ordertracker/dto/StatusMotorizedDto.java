package com.inretailpharma.digital.ordertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusMotorizedDto {
    private String statusName;
    private Long statusDate;
}
