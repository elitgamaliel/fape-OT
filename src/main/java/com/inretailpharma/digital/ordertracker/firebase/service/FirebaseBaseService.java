package com.inretailpharma.digital.ordertracker.firebase.service;

import java.util.List;

import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.core.TokenInfo;

public class FirebaseBaseService implements FirebaseService  {

	@Override
	public TokenInfo parseToken(String idToken) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(String path, FirebaseModel model) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void save(String path, List<FirebaseModel> models) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void update(String path, FirebaseModel... models) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void delete(String path, String id) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void getByKey(String path, String key, FirebaseDataListener listener) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void newUser(CreateRequest createRequest) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void updateUser(UpdateRequest createRequest) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public UserRecord findUserByEmail(String email) {
		throw new UnsupportedOperationException();
	}

}
