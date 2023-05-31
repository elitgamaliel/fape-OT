package com.inretailpharma.digital.ordertracker.repository.device;

import com.inretailpharma.digital.ordertracker.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
}
