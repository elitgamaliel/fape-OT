package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@Embeddable
@Entity
@Table(name = "user_status_history")
public class UserStatusHistory implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @Column(name="time_from_ui")
    private Date timeFromUi;
    @Column(name="order_external_id")
    private Long orderExternalId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_code")
    private TrackingStatus status;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;
    @Column(name="local_code")
    private String localCode;
    @Column(name="parent_id")
    private Long parentId;

    public UserStatusHistory(TrackingStatus status, Date timeFromUi) {
        this.status = status;
        this.timeFromUi = timeFromUi;
    }

    public UserStatusHistory(TrackingStatus status, Date timeFromUi, BigDecimal latitude, BigDecimal longitude) {
        this.status = status;
        this.timeFromUi = timeFromUi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
