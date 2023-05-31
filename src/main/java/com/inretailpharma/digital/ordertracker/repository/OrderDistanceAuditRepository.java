package com.inretailpharma.digital.ordertracker.repository;

import com.inretailpharma.digital.ordertracker.entity.OrderDistanceAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDistanceAuditRepository extends JpaRepository<OrderDistanceAudit, Long> {

}
