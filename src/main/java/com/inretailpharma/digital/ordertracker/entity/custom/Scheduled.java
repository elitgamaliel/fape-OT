package com.inretailpharma.digital.ordertracker.entity.custom;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Data
@Embeddable
public class Scheduled {

    @Column(name = "scheduled_start_date")
    private LocalDateTime startDate;
    @Column(name = "scheduled_end_date")
    private LocalDateTime endDate;
}
