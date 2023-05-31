package com.inretailpharma.digital.ordertracker.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderStatusDto implements Serializable{
    private Long orderExternalId;
    private String orderTrackingCode;
    private String status;
    private Double latitude;
    private Double longitude;
    private String code;
    private String detail;
    private String note;
    private String description;
    //este campo se usa para saber si la peticion viene de la APP (OmniTracker) o de la WEB (Despachador), en caso WEB viene null ya que esta no se modifico  
    private String origin;
    private Long creationDate;
    private Boolean createdOffline;
    private String previousStatus;
    private String updatedBy;
    
    private String sourceName;
    private String statusDetail;
    private String timeFromUi;
    private String target;
    private boolean auditable; 
}
