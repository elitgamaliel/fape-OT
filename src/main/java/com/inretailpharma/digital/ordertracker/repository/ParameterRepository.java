package com.inretailpharma.digital.ordertracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inretailpharma.digital.ordertracker.entity.ApplicationParameter;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<ApplicationParameter, String> {
    @Query("SELECT a FROM ApplicationParameter a WHERE a.enabled = 1")
    List<ApplicationParameter> findAllActiveParameters();
}
