package com.inretailpharma.digital.ordertracker.firebase.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.inretailpharma.digital.ordertracker.config.auth.FirebaseTokenInvalidException;
import com.inretailpharma.digital.ordertracker.exception.UserException;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderTrackerFirebaseService implements FirebaseService {

    @Qualifier("orderTrackerDatabase")
    @Autowired
    private DatabaseReference databaseReference;

    @Qualifier("orderTrackerFirebaseAuthToken")
    @Autowired
    private GoogleCredential scoped;


    @Override
    public TokenInfo parseToken(String firebaseToken) {
        if (StringUtils.isEmpty(firebaseToken)) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);

            return TokenInfo.builder()
                    .uid(token.getUid())
                    .email(token.getEmail())
                    .build();

        } catch (Exception e) {
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }

    @Override
    public void save(String path, FirebaseModel model) {
        log.info(">>OT New object: {} in path: {}", model.getId(), path);
        DatabaseReference childReference = databaseReference.child(path);
        childReference.child(model.getId()).setValue(model.getValue(), null);
    }

    @Override
    public void save(String path, List<FirebaseModel> models) {
        log.info(">>OT New objects: {} in path: {}", models.stream().map(FirebaseModel::getId).collect(Collectors.toList()), path);
        DatabaseReference childReference = databaseReference.child(path);
        models.forEach(model -> {
            log.info(model.toString());
            childReference.child(model.getId()).setValue(model.getValue(), null);
        });
    }

    @Override
    public void update(String path, FirebaseModel... models) {
        log.info(">>OT Path: {} updated with: {}", path, Arrays.asList(models));
        DatabaseReference childReference = databaseReference.child(path);
        Map<String, Object> objectToUpdated = new HashMap<>();
        for (FirebaseModel model : models) {
            objectToUpdated.put(model.getId(), model.getValue());
        }
        childReference.updateChildren(objectToUpdated, null);
    }

    @Override
    public void delete(String path, String id) {
        log.info(">>OT Deleted id: {} from path: {}", id, path);
        DatabaseReference childReference = databaseReference.child(path);
        Map<String, Object> objectToUpdated = new HashMap<>();
        objectToUpdated.put(id, null);
        childReference.updateChildren(objectToUpdated, (databaseError, dbRef) -> {

        });
    }

    @Override
    public void getByKey(String path, String key, FirebaseDataListener listener) {
        log.info(">>OT Find key {} in path: {}", key, path);
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
    
    @Override
    public void newUser(CreateRequest createRequest) {
    	try {
        	UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
        	log.info("Successfully created new user in Firebase: " + userRecord.getUid());
        }catch(Exception ex) {
        	log.error("Error create user in Firebase.", ex);
        	throw new UserException("Error create user in Firebase");
        }
    }
    
    @Override
    public void updateUser(UpdateRequest updateRequest) {
    	try {
			UserRecord userRecord = FirebaseAuth.getInstance().updateUser(updateRequest);
        	log.info("Successfully update user in Firebase: " + userRecord.getUid());
        }catch(Exception ex) {
        	log.error("Error update user in Firebase.", ex);
        	throw new UserException("Error update user in Firebase");
        }
    }
    
    @Override
    public UserRecord findUserByEmail(String email) {
    	try {
			return FirebaseAuth.getInstance().getUserByEmail(email);
		} catch (Exception ex) {
			log.error("Error searching user in Firebase.", ex);
        	throw new UserException("Error searching user in Firebase");
		}
    }
}
