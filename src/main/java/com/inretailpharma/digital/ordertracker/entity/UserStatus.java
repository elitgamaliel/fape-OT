package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Henry Gonzales Segovia
 * @version 9/11/2017
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "status")
@DiscriminatorValue("USER")
public class UserStatus extends Status {

    public UserStatus() {
    }

    public UserStatus(Code code) {
        super(code);
    }
}
