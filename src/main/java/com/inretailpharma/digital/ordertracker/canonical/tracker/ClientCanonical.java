package com.inretailpharma.digital.ordertracker.canonical.tracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ClientCanonical {
    private String documentNumber;
    private String fullName;
    private String email;
    private String phone;
}
