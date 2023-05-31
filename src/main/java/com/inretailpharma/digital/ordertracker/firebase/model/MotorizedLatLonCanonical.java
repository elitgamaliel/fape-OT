package com.inretailpharma.digital.ordertracker.firebase.model;

import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MotorizedLatLonCanonical implements FirebaseObject {
    private Double latitude;
    private Double longitude;

}
