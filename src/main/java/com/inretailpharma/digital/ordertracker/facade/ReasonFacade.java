package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.entity.Reason;
import com.inretailpharma.digital.ordertracker.service.reason.ReasonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ReasonFacade {

    @Autowired
    private ReasonService reasonService;

   public  List<Reason> getAllReasons(){
       return reasonService.getAllReasons();
   }
}
