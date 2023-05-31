package com.inretailpharma.digital.ordertracker.service.parameter;

import com.inretailpharma.digital.ordertracker.entity.ApplicationParameter;

import java.util.List;

public interface ParameterService {
    ApplicationParameter parameterByCode(String code);

    List<ApplicationParameter> getAllParameters();

    List<ApplicationParameter> getAllActiveParameters();

    void saveParameter(ApplicationParameter parameter);
}
