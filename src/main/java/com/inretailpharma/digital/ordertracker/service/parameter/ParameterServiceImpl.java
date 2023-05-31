package com.inretailpharma.digital.ordertracker.service.parameter;


import com.inretailpharma.digital.ordertracker.entity.ApplicationParameter;
import com.inretailpharma.digital.ordertracker.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    ParameterRepository parameterRepository;

    @Override
    public ApplicationParameter parameterByCode(String code) {
        return parameterRepository.getOne(code);
    }

    @Override
    public List<ApplicationParameter> getAllParameters() {
        return parameterRepository.findAll();
    }

    @Override
    public List<ApplicationParameter> getAllActiveParameters() {
        return parameterRepository.findAllActiveParameters();
    }

    @Override
    public void saveParameter(ApplicationParameter parameter) {
        parameterRepository.save(parameter);
    }
}
