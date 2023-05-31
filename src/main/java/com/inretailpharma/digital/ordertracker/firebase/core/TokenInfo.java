package com.inretailpharma.digital.ordertracker.firebase.core;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo {
    private String clientId;
    private String email;
    private String name;
    private String uid;
    private String userId;
    private String pictureUrl;
    private boolean enabled;
    private boolean validated;
}
