package com.inretailpharma.digital.ordertracker.transactions;

import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.service.user.FirebaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
@Component
public class DeviceTransaction {

    @Autowired
    FirebaseUserService firebaseUserService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void saveDevice(DeviceDto deviceDto) {
        firebaseUserService.saveDeviceAndAssociateToCurrentMobileUser(deviceDto);
    }
}
