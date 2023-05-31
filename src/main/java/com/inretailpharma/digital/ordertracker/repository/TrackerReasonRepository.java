package com.inretailpharma.digital.ordertracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inretailpharma.digital.ordertracker.entity.TrackerReason;

public interface TrackerReasonRepository extends JpaRepository<TrackerReason, Integer> {
	
    List<TrackerReason> findAllByType(TrackerReason.Type type);
    
}