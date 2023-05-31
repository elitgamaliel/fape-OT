package com.inretailpharma.digital.ordertracker.facade;

import java.util.ArrayList;
import java.util.List;

import com.inretailpharma.digital.ordertracker.dto.setting.SettingParameterDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.AlertDto;
import com.inretailpharma.digital.ordertracker.dto.SettingDto;
import com.inretailpharma.digital.ordertracker.entity.Alert;
import com.inretailpharma.digital.ordertracker.entity.ApplicationParameter;
import com.inretailpharma.digital.ordertracker.service.alert.AlertService;
import com.inretailpharma.digital.ordertracker.service.parameter.ParameterService;
import com.inretailpharma.digital.ordertracker.utils.Constant;

@Component
public class SystemTrackerFacade {
	
	
	@Autowired
	private ParameterService parameterService;
	
	@Autowired
	private AlertService alertService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public List<SettingDto> findAllSettingsAsCanonical() {
        List<ApplicationParameter> parameters = parameterService.getAllParameters();
        List<SettingDto> canonicalList = new ArrayList<>();
        parameters.forEach(parameter -> {
            SettingDto canonical = SettingDto.builder()
            		.code(parameter.getCode())
            		.value(parameter.getValue())
            		.description(parameter.getDescription())
            		.build();
            canonicalList.add(canonical);
        });
        return canonicalList;
    }

    public List<SettingDto> findAllSettingsActiveAsCanonical() {
        List<ApplicationParameter> parameters = parameterService.getAllActiveParameters();
        List<SettingDto> canonicalList = new ArrayList<>();
        parameters.forEach(parameter -> {
            SettingDto canonical = SettingDto.builder()
                    .code(parameter.getCode())
                    .value(parameter.getValue())
                    .description(parameter.getDescription())
                    .build();
            canonicalList.add(canonical);
        });
        return canonicalList;
    }

    public SettingParameterDto updateSettingParameter(SettingDto settingDto) {
        SettingParameterDto parameterObj = new SettingParameterDto();
        ApplicationParameter parameter = parameterService.parameterByCode(settingDto.getCode());
        parameter.setValue(settingDto.getValue());
        parameter.setDescription(settingDto.getDescription());
        parameterService.saveParameter(parameter);

        parameterObj.setId(settingDto.getCode());
        return parameterObj;
    }

    public SettingDto getVeryfiPhoneBySms() {
    	ApplicationParameter applicationParameter = parameterService.parameterByCode(Constant.VERIFY_CELLPHONE_BY_SMS);
    	return new SettingDto(applicationParameter.getValue(),applicationParameter.getDescription());
    }
    
    public List<AlertDto> getAlertByClient(String appname) {
        List<Alert> alertList = alertService.getAlertByClient(appname);
        List<AlertDto> canonicalList = new ArrayList<>();
        alertList.forEach(alert -> canonicalList.add(modelMapper.map(alert, AlertDto.class)));
        return canonicalList;
    }
}
