package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Alert implements Serializable {

    public static final String ALERT_ORDER_DELIVERY_TIME = "A01";

    @Id
    private String id;
    private String message;
    @ElementCollection
    @CollectionTable(name = "alert_scope", joinColumns = @JoinColumn(name = "alert_id"))
    private List<AlertScope> scopeList;

}
