package com.inretailpharma.digital.ordertracker.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Table;

import java.io.Serializable;


@Data
@Embeddable
@Table(name = "alert_scope")
public class AlertScope implements Serializable {

    private String tag;
    private String scope;

}
