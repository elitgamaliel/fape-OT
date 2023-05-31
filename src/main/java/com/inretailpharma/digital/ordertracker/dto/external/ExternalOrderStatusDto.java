package com.inretailpharma.digital.ordertracker.dto.external;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalOrderStatusDto implements Serializable {

    private String statusName;
    private Long statusDate;
    private String statusNote;
    private Double latitude;
    private Double longitude;
    private String updatedBy;
    private String customNote;
    private String payBackEnvelope;
    private String statusDrugstore;
    //este campo se usa para saber si la peticion viene de la APP (OmniTracker) o de la WEB (Despachador), en caso WEB viene null ya que esta no se modifico
    private String origin;
    private String code;
    private String action;
}
