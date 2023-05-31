package com.inretailpharma.digital.ordertracker.firebase.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Map;

public interface FirebaseObject extends Serializable {

    @SuppressWarnings("unchecked")
    default Map<String, Object> firebaseMapper() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, Map.class);
    }
}
