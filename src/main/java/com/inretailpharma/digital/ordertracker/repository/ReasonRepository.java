package com.inretailpharma.digital.ordertracker.repository;

import com.inretailpharma.digital.ordertracker.entity.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, String> {
}