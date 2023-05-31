package com.inretailpharma.digital.ordertracker.service.external;

import com.inretailpharma.digital.ordertracker.dto.in.SendSmsIn;

public interface ExternalSmsService {
    void sendSms(SendSmsIn sendSmsIn);
}
