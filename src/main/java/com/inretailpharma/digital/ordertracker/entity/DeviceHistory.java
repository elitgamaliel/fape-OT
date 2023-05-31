package com.inretailpharma.digital.ordertracker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "device_history")
public class DeviceHistory implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorized_id")
    private User user;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assigned_time")
    private Date assignedTime;

}
