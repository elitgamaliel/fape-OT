package com.inretailpharma.digital.ordertracker.firebase.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FirebaseModel implements Serializable {

    private String id;
    private Object value;

    private <T> FirebaseModel(String id, T value) {
        this.id = id;
        this.value = value;
    }

    public static FirebaseModel create(String id, Boolean value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, String value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, Integer value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, Long value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, Double value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, Object value) {
        return new FirebaseModel(id, value);
    }

    public static FirebaseModel create(String id, FirebaseObject value) {
        return new FirebaseModel(id, value.firebaseMapper());
    }

    public static FirebaseModel create(String id, List<FirebaseObject> value) {
        List<Map<String, Object>> wrapperList = value.stream().map(FirebaseObject::firebaseMapper).collect(Collectors.toList());
        return new FirebaseModel(id, wrapperList);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{\"FirebaseModel\":{"
                + "\"id\":\"" + id + "\""
                + ", \"value\":" + value
                + "}}";
    }
}
