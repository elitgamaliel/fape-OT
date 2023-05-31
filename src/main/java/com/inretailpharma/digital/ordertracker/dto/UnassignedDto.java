package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnassignedDto implements Serializable {

    private String groupName;
    private String motorizedId;
    private List<Long> orders;
    private List<String> orderTrackingCodes;
    private String source;
    private String updateBy;
}