package com.inretailpharma.digital.ordertracker.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleDto implements Serializable {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
