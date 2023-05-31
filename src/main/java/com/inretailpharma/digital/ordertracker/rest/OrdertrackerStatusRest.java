package com.inretailpharma.digital.ordertracker.rest;

import java.util.List;

import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.AssignedOrdersResponseDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderDto;
import com.inretailpharma.digital.ordertracker.dto.SyncOrderResponseDto;
import com.inretailpharma.digital.ordertracker.dto.UnassignedDto;
import com.inretailpharma.digital.ordertracker.facade.OrderFacade;

import io.swagger.annotations.Api;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@Api(value = "OrdertrackerStatusRest", produces = "application/json")
public class OrdertrackerStatusRest {

    private OrderFacade orderFacade;

    public OrdertrackerStatusRest(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping("orders/status/assigned")
    public Mono<ResponseEntity<AssignedOrdersResponseDto>> 
    	assignOrders(@RequestBody ProjectedGroupDto projectedGroupDto) {
    	
        return orderFacade.assignOrders(projectedGroupDto)
        		.map(r -> ResponseEntity
    	                .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
    	                .body(r))
    	        .subscribeOn(Schedulers.parallel());
    }

    @PatchMapping("orders/status/unassigned")
    public Mono<ResponseEntity<String>> unassignOrders(@RequestBody UnassignedDto unassignedDto) {
        
    	return orderFacade.unassignOrders(unassignedDto)
    	        .map(r -> ResponseEntity
    	                .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
    	                .body(r))
    	        .subscribeOn(Schedulers.parallel());
    }

    @PatchMapping("order/{ecommerceId}/status/{status}")
    public Mono<ResponseEntity<String>> updateOrderStatus(
            @PathVariable(name = "ecommerceId") Long ecommerceId,
            @PathVariable(name = "status") String status) {
    	
        return orderFacade.kitStatusUpdate(ecommerceId, status.toUpperCase(), null)
	        .map(r -> ResponseEntity
	                .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
	                .body(r))
	        .subscribeOn(Schedulers.parallel());
    }        
    
    @PutMapping("/v2/orders/status/synchronize")
    @Secured({"ROLE_MOTORIZED"})
    public Flux<SyncOrderResponseDto> synchronizeOrders(@RequestBody List<SyncOrderDto> orders) {

        log.info("[START] endpoint PATCH orders/status/synchronize - {}", orders);

        return orderFacade.synchronizeOrders(orders, SecurityUtils.getUID());
    }

    //old version
    @PutMapping("orders/status/synchronize")
    @Secured({"ROLE_MOTORIZED"})
    public void synchronizeOrders() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }
}
