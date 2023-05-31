package com.inretailpharma.digital.ordertracker.entity;

import com.inretailpharma.digital.ordertracker.entity.custom.UserShiftId;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_shift")
public class UserShift implements Serializable {

    @EmbeddedId
    private UserShiftId id;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "is_active")
    private Constant.Logical isActive;

}