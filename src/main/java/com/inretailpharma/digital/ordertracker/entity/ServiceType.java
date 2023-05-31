package com.inretailpharma.digital.ordertracker.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "service_type")
public class ServiceType implements Serializable {
	
	@Id
    @Enumerated(EnumType.STRING)
    private Constant.ServiceType code;
    private String description;

}
