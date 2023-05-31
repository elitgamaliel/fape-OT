package com.inretailpharma.digital.ordertracker.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SendSmsIn {
	@NotNull
    @Pattern(regexp = "^\\d{9,}$")
    private String phoneNumber;
    private String content;
}
