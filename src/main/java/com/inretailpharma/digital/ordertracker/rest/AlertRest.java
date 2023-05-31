package com.inretailpharma.digital.ordertracker.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.dto.AlertDto;
import com.inretailpharma.digital.ordertracker.facade.SystemTrackerFacade;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("alerts")
public class AlertRest {

	@Autowired
	private SystemTrackerFacade systemTrackerFacade;
	
	@GetMapping
    public List<AlertDto> allAlerts(@RequestParam String appname) {
        List<AlertDto> alertList = systemTrackerFacade.getAlertByClient(appname);
        log.info("# Total rows: {}", alertList.size());
        return alertList;
    }
	
}
