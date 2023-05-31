package com.inretailpharma.digital.ordertracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inretailpharma.digital.ordertracker.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, String> {

    @Query(value = "select distinct a from Alert a join a.scopeList s where s.tag in (:tagList)")
    List<Alert> findAllByTagList(@Param("tagList") List<String> tagList);
}