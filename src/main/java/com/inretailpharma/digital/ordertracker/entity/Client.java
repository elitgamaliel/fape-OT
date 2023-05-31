package com.inretailpharma.digital.ordertracker.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.inretailpharma.digital.ordertracker.entity.core.TrackerEntity;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.Data;

@Data
@Entity
@Table(name = "client")
public class Client extends TrackerEntity<Long> {

    private String dni;
    private String phone;
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "inka_club_client")
    private String inkaClubClient;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_anonymous")
    @Enumerated(EnumType.STRING)
    private Constant.Logical isAnonymous;

}
