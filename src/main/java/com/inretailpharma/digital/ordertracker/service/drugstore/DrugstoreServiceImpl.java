package com.inretailpharma.digital.ordertracker.service.drugstore;

import com.inretailpharma.digital.ordertracker.entity.DrugstoreGroup;
import com.inretailpharma.digital.ordertracker.repository.DrugstoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugstoreServiceImpl implements DrugstoreService {

    @Autowired
    private DrugstoreRepository drugstoreRepository;

    @Override
    public DrugstoreGroup findGroupForId(Integer id) {
        return drugstoreRepository.findById(id).get();
    }
}
