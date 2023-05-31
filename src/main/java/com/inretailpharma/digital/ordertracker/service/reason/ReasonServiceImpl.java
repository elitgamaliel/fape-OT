package com.inretailpharma.digital.ordertracker.service.reason;

import com.inretailpharma.digital.ordertracker.entity.Reason;
import com.inretailpharma.digital.ordertracker.repository.ReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonServiceImpl implements ReasonService {
    @Autowired
    private ReasonRepository reasonRepository;

    @Override
    public List<Reason> getAllReasons() {
        return reasonRepository.findAll();
    }
}