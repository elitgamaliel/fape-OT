package com.inretailpharma.digital.ordertracker.repository;

import com.inretailpharma.digital.ordertracker.entity.Shift;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findAllByIsEnabled(Constant.Logical isEnabled);
}
