package com.inretailpharma.digital.ordertracker.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * @author Henry Gonzales Segovia
 * @version 1/09/2017
 */
@Data
@Entity
@ToString
@Table(name = "user")
public class User {

	@Id
    private String id;
    @Column(name = "employee_code")
    private String employeeCode;
    
    private String dni;
    private String phone;
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String alias;
    @Column(name = "url_photo")
    private String urlPhoto;
    @Column(name = "end_break_status_date")
    private Date endBreakStatusDate;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_code"))
    private List<Role> roleList;
    @ManyToOne
    @JoinColumn(name = "user_status")
    private UserStatus userStatus;
    @ManyToOne
    @JoinColumn(name = "tracking_status")
    private TrackingStatus trackingStatus;
    @Column(name = "drugstore_id")
    private Long drugstoreId;
    @Column(name = "drugstore_group_id")
    private Integer drugstoreGroupId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_id")
    private Transport transport;
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<UserStatusHistory> statusHistoryList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private MotorizedType type;
    
    @Column(name = "current_local")
    private String currentLocal;

    @Column(name = "current_version")
    private String currentVersion;
}
