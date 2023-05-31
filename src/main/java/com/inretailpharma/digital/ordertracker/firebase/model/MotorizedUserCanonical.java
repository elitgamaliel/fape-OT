package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;

@Data
public class MotorizedUserCanonical implements FirebaseObject {
    private String alias;
    private String phone;
    private String photo;
    private String firstName;
    private String lastName;
    private String dni;
}
