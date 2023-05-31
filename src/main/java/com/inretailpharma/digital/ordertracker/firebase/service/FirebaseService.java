package com.inretailpharma.digital.ordertracker.firebase.service;

import java.util.List;

import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

public interface FirebaseService {

    TokenInfo parseToken(String idToken);

    void save(String path, FirebaseModel model);

    void save(String path, List<FirebaseModel> models);

    void update(String path, FirebaseModel... models);

    void delete(String path, String id);

    void getByKey(String path, String key, FirebaseDataListener listener);
    
    public void newUser(CreateRequest createRequest);
    
    public void updateUser(UpdateRequest createRequest);
    
    public UserRecord findUserByEmail(String email);
}
