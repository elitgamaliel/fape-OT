package com.inretailpharma.digital.ordertracker.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EtaDto implements Serializable {

    private Integer projected;
    private Integer accumulated;

    public EtaDto(Integer projected, Integer accumulated) {
        this.projected = projected;
        this.accumulated = accumulated;
    }
}

