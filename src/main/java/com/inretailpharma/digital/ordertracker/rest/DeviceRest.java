package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.facade.DeviceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceRest {

    @Autowired
    DeviceFacade deviceFacade;

    @PostMapping
    @Secured({"ROLE_MOTORIZED", "ROLE_PICKER"})
    public void saveDevice(@RequestBody DeviceDto deviceDto) {
        deviceFacade.saveDevice(deviceDto);
    }
}
