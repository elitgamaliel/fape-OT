package com.inretailpharma.digital.ordertracker.dto;

import com.inretailpharma.digital.ordertracker.dto.resume.OrderTimeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ReturnResumeDto implements Serializable {

    private List<OrderTimeDto> orderTimes;
    private long timespan;
}
