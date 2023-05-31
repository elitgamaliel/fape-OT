package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.dto.SettingDto;
import com.inretailpharma.digital.ordertracker.dto.setting.SettingParameterDto;
import com.inretailpharma.digital.ordertracker.facade.SystemTrackerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
public class SettingRest {

    @Autowired
    private SystemTrackerFacade systemTrackerFacade;

    @GetMapping
    @Secured({"ROLE_ADMINISTRATOR", "ROLE_MOTORIZED"})
    public List<SettingDto> getAllSettings() {
        return systemTrackerFacade.findAllSettingsAsCanonical();
    }

    @GetMapping("/verify-phone-by-sms")
    public SettingDto verifyPhoneBySmsRest() {
        return systemTrackerFacade.getVeryfiPhoneBySms();
    }

    @GetMapping("/active")
    public List<SettingDto> getAllSettingsActive() {
        return systemTrackerFacade.findAllSettingsActiveAsCanonical();
    }

    @PutMapping("/active/{code}")
    //@Secured("ROLE_ADMINISTRATOR")
    public SettingParameterDto updateSettingParameter(@RequestBody SettingDto settingCanonical,
                                                      @PathVariable(name="code") String code){
        return systemTrackerFacade.updateSettingParameter(settingCanonical);
    }

}
