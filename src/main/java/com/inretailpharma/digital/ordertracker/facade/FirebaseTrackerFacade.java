package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.service.user.FirebaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
public class FirebaseTrackerFacade {
    @Autowired
    FirebaseUserService firebaseUserService;

}
