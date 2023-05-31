package com.inretailpharma.digital.ordertracker.entity.custom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Henry Gonzales Segovia
 * @version 7/09/2017
 */
@Entity
public class Profit implements Serializable {

    @Id
    @Temporal(TemporalType.DATE)
    private Date date;
    private Integer total;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{\"Profit\":{"
            + "\"date\":" + date
            + ", \"total\":\"" + total + "\""
            + "}}";
    }
}
