package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.entity.core.TrackerEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Transport extends TrackerEntity<Long> {

	@Column(name="license_plate")
    private String licensePlate;
    private String color;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_type_id")
    private TransportType type;

}
