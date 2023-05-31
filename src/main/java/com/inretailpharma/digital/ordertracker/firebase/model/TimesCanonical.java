package com.inretailpharma.digital.ordertracker.firebase.model;


import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseObject;

import lombok.Data;

@Data
public class TimesCanonical implements FirebaseObject {

    private Integer eta;
    private Integer etap;
    private Integer tcr;
    private Long orderExternalId;
}
