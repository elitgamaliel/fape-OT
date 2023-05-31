package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.entity.Reason;
import com.inretailpharma.digital.ordertracker.facade.ReasonFacade;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(value = "ReasonRest", produces = "application/json")
public class ReasonRest {

    @Autowired
    private ReasonFacade reasonFacade;

    @GetMapping("/reason")
    @Secured({"ROLE_MOTORIZED"})
    public List<Reason> getAllReasons() {
        return reasonFacade.getAllReasons();
    }

}