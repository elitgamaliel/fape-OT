package com.inretailpharma.digital.ordertracker.repository;

import com.inretailpharma.digital.ordertracker.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <Client, Long> {

    Client findFirstByDniOrderByIdDesc(String dni);	
    Client findFirstByPhoneOrderByIdDesc (String phone);
}
