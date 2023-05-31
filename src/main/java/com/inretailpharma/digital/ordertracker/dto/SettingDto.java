package com.inretailpharma.digital.ordertracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingDto implements Serializable {

    private String code;
    private String value;
    private String description;
    private Integer enabled;

    public SettingDto(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
