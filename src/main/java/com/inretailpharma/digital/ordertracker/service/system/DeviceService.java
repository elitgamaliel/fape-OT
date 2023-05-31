package com.inretailpharma.digital.ordertracker.service.system;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.entity.Device;
import com.inretailpharma.digital.ordertracker.entity.DeviceHistory;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.repository.device.DeviceRepository;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

@Slf4j
@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public Device findDeviceForImei(User user, String imei) {
        log.info("[START] findDeviceForImei - user.getId(): {}, imei: {}", user.getId(), imei);

        Optional<Device> deviceOptional = deviceRepository.findById(imei);
        
        return deviceOptional.map(device -> {
            log.info("findDeviceForImei - device.getUser().getId(): {}", device.getUser().getId());
        	
        	if (!device.getUser().getId().equals(user.getId())) {
                log.info("findDeviceForImei - if(true)");
                device.setUser(user);
                device.getDeviceHistoryList().add(new DeviceHistory(user, DateUtil.currentDate()));
                deviceRepository.save(device);
            }  
        	return device;
        }).orElse(null);

    }
}
