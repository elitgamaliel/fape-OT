package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "drugstore_group")
public class DrugstoreGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "group_name")
    private String groupName;
    @Enumerated(EnumType.STRING)
    @Column(name = "group_status")
    private Constant.Logical groupStatus;
    @Column(name = "parent_company_code")
    private String parentCompanyCode;
    @Column(name = "facility_code")
    private String facilityCode;
    @Column(name = "company_code")
    private String companyCode;
    @ElementCollection
    @CollectionTable(name = "drugstore_mapping", joinColumns = @JoinColumn(name = "group_id"))
    @Column(name = "drugstore_id")
    private List<Long> mapping;

}
