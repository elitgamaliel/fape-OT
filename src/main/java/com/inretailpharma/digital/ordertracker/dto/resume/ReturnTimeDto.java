package com.inretailpharma.digital.ordertracker.dto.resume;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnTimeDto implements Serializable {
    private Integer time;
    private Integer delay;
}
