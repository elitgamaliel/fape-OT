package com.inretailpharma.digital.ordertracker.repository;

import com.inretailpharma.digital.ordertracker.entity.DrugstoreGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugstoreRepository extends JpaRepository<DrugstoreGroup, Integer> {


}
