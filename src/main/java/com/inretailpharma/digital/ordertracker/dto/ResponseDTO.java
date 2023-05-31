package com.inretailpharma.digital.ordertracker.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {
    private String code;
    private String title;
    private String message;
    private T data;

    public ResponseDTO() {
    }
    
    public ResponseDTO(String code) {
        this.code = code;
    }

    public ResponseDTO(String code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
    }

    public ResponseDTO(String code, String title, String message, T data) {
        this.code = code;
        this.title = title;
        this.message = message;
        this.data = data;
    }

}
