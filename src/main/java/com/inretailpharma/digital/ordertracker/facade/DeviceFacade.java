package com.inretailpharma.digital.ordertracker.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.transactions.DeviceTransaction;

@Component
public class DeviceFacade {

	@Autowired
	DeviceTransaction deviceTransaction;

	public void saveDevice(DeviceDto deviceDto) {
		deviceTransaction.saveDevice(deviceDto);
	}

}
