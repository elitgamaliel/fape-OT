package com.inretailpharma.digital.ordertracker.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "tracker_reason")
public class TrackerReason implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String code;
    private String reason;

    public enum Type {
        CANCELLED, REJECTED
    }
}
