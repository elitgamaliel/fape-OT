package com.inretailpharma.digital.ordertracker.utils;

public class ObjectUnique<T> {

    private T value;

    public ObjectUnique(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
