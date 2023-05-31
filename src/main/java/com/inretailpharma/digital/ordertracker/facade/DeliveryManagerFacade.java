package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderCanonical;
import com.inretailpharma.digital.ordertracker.dto.PartialOrderDto;
import com.inretailpharma.digital.ordertracker.transactions.OrderTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class DeliveryManagerFacade {
	
    @Autowired
    private OrderTransaction orderTransaction;

    public Mono<OrderCanonical> getUpdatePartialOrder(PartialOrderDto partialOrderDto) {
        log.info("[START] getUpdatePartialOrder:{}",partialOrderDto);

        return Mono
                .just(orderTransaction.updatePartialOrder(partialOrderDto))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    log.error("Error during update partial order:{}",e);
                    return null;
                });

    }
}
