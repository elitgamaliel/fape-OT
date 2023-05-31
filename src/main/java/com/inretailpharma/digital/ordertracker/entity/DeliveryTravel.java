package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "delivery_travel")
public class DeliveryTravel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "motorized_id")
    private String motorizedId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status")
    private TravelStatus travelStatus;
    @Column(name = "projected_eta")
    private Integer projectedEta;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "pick_up_center_name")
    private String pickUpCenterName;
    @Column(name = "pick_up_center_latitude")
    private BigDecimal pickUpCenterLatitude ;
    @Column(name = "pick_up_center_longitude")
    private BigDecimal pickUpCenterLongitude ;
    
    @ElementCollection
    @CollectionTable(name = "delivery_travel_detail", joinColumns = @JoinColumn(name = "delivery_travel_id"))
    private List<DeliveryTravelDetail> detailList;
    
    public enum TravelStatus {
        CREATED, IN_PROGRESS, CANCELLED, FINALIZED
    }
}
