package com.inretailpharma.digital.ordertracker.service.drugstore;

import com.inretailpharma.digital.ordertracker.entity.DrugstoreGroup;

public interface DrugstoreService {
    DrugstoreGroup findGroupForId(Integer id);
}
