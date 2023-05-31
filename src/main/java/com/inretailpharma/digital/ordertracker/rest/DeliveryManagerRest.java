package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderCanonical;
import com.inretailpharma.digital.ordertracker.dto.OrderDto;
import com.inretailpharma.digital.ordertracker.dto.PartialOrderDto;
import com.inretailpharma.digital.ordertracker.facade.DeliveryManagerFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/nvr/order")
@Slf4j
public class DeliveryManagerRest {

    @Autowired
    private DeliveryManagerFacade deliveryManagerFacade;

    @PostMapping("/partial")
    public Mono<OrderCanonical> updatePartialOrder(@RequestBody PartialOrderDto partialOrderDto) {
        log.info("[START] endpoint updatePartialOrder /order/partial/ - partialOrderDto: {}",
                partialOrderDto);

        return deliveryManagerFacade.getUpdatePartialOrder(partialOrderDto);
    }
}
