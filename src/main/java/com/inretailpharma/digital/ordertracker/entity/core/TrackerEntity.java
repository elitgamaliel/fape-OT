package com.inretailpharma.digital.ordertracker.entity.core;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


@MappedSuperclass
@SuppressWarnings("all")
public abstract class TrackerEntity<T extends Serializable> {

    /**
     * Unique reference for this entity
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{\"TrackerEntity\":{"
            + "\"id\":" + id
            + "}}";
    }
}
