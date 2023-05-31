package com.inretailpharma.digital.ordertracker.firebase.service;

import com.google.firebase.database.DataSnapshot;


public interface FirebaseDataListener {

    void onDataValue(DataSnapshot dataSnapshot);
}
