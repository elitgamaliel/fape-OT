package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "motorized_type")
public class MotorizedType implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private Constant.MotorizedType code;
    private String description;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
    		name = "motorized_service",
    		joinColumns = @JoinColumn(name = "motorized_type_code"),
    		inverseJoinColumns = @JoinColumn(name = "service_type_code"))
    private List<ServiceType> services;
    
    public MotorizedType(String type) {
    	this.code = Constant.MotorizedType.parseByName(type);
    }
}
