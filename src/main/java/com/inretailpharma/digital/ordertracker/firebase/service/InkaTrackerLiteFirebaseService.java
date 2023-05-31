package com.inretailpharma.digital.ordertracker.firebase.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InkaTrackerLiteFirebaseService extends FirebaseBaseService implements FirebaseService{

    @Qualifier("inkaTrackerLiteDatabase")
    @Autowired
    private DatabaseReference databaseReference;

    @Qualifier("inkaTrackerLiteFirebaseAuthToken")
    @Autowired
    private GoogleCredential scoped;


    @Override
    public TokenInfo parseToken(String idToken) {
        return null;
    }

    @Override
    public void save(String path, FirebaseModel model) {
        log.info(">>IKL New object: {} in path: {}", model.getId(), path);
        DatabaseReference childReference = databaseReference.child(path);
        childReference.child(model.getId()).setValue(model.getValue(), null);
    }

    @Override
    public void save(String path, List<FirebaseModel> models) {
    	log.info(">>IKL New objects: {} in path: {}", models.stream().map(FirebaseModel::getId).collect(Collectors.toList()), path);
        DatabaseReference childReference = databaseReference.child(path);
        models.forEach(model -> {
        	log.info(model.toString());
            childReference.child(model.getId()).setValue(model.getValue(), null);
        });
    }

    @Override
    public void update(String path, FirebaseModel... models) {
    	log.info(">>IKL Path: {} updated with: {}", path, Arrays.asList(models));
        DatabaseReference childReference = databaseReference.child(path);
        Map<String, Object> objectToUpdated = new HashMap<>();
        for (FirebaseModel model : models) {
            objectToUpdated.put(model.getId(), model.getValue());
        }
        childReference.updateChildren(objectToUpdated, null);
    }

    @Override
    public void delete(String path, String id) {
    	log.info(">>IKL Deleted id: {} from path: {}", id, path);
        DatabaseReference childReference = databaseReference.child(path);
        Map<String, Object> objectToUpdated = new HashMap<>();
        objectToUpdated.put(id, null);
        childReference.updateChildren(objectToUpdated, null);
    }

    @Override
    public void getByKey(String path, String key, FirebaseDataListener listener) {
    	log.info(">>IKL Find key {} in path: {}", key, path);
        DatabaseReference childReference = databaseReference.child(path);
        childReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onDataValue(dataSnapshot);
                } else {
                    throw new DatabaseException(">> No results found for the search");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public String getToken() throws IOException {

        scoped.refreshToken();

        return scoped.getAccessToken();

    }
}
