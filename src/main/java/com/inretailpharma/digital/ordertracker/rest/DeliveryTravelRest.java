package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.CancelTravelDto;
import com.inretailpharma.digital.ordertracker.dto.TravelDto;
import com.inretailpharma.digital.ordertracker.facade.OrderFacade;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
@Slf4j
@RestController
public class DeliveryTravelRest {

	private OrderFacade orderFacade;

    public DeliveryTravelRest(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }
    
    @PostMapping("/v2/delivery-travel")
    @Secured({"ROLE_MOTORIZED"})
    public Mono<ResponseEntity<String>> createDeliveryTravel(@RequestBody TravelDto travelDto) {
        log.info("[START] endpoint createTravel /travel/{}",travelDto);
      return  orderFacade.createTravel(travelDto, SecurityUtils.getUID(), SecurityUtils.getUID(), Constant.MotorizedType.DRUGSTORE).map(r -> ResponseEntity
              .status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_STREAM_JSON)
              .body(r))
              .defaultIfEmpty(ResponseEntity.notFound().build())
              .doOnSuccess(r -> log.info("[END] endpoint createTravel /travel/{}",travelDto))
              .subscribeOn(Schedulers.parallel());
    }
    
    @DeleteMapping("/delivery-travel")
    @Secured({"ROLE_MOTORIZED"})
    public Mono<ResponseEntity<String>> cancelDeliveryTravel() {
        log.info("[START] endpoint cancelTravel");
        return orderFacade.cancelTravel(SecurityUtils.getUID(), SecurityUtils.getUID(), Constant.MotorizedType.DRUGSTORE).map(r -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(r))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(r -> log.info("[END] endpoint cancelTravel "))
                .subscribeOn(Schedulers.parallel());
    }
    
    @PostMapping("/nvr/delivery-travel")
    public Mono<ResponseEntity<String>> nvrCreateDeliveryTravel(@RequestBody TravelDto travelDto) {
        log.info("[START] endpoint nvr/createTravel /travel/{}",travelDto);
        
        
      return  orderFacade.nvrCreateTravel(travelDto, travelDto.getMotorizedId(), "OPE").map(r -> ResponseEntity
              .status(HttpStatus.OK)
              .contentType(MediaType.APPLICATION_STREAM_JSON)
              .body(r))
              .defaultIfEmpty(ResponseEntity.notFound().build())
              .doOnSuccess(r -> log.info("[END] endpoint createTravel /travel/{}",travelDto))
              .subscribeOn(Schedulers.parallel());
    }
    
    @DeleteMapping("/nvr/delivery-travel")
    public Mono<ResponseEntity<String>> nvrCancelDeliveryTravel(@RequestBody CancelTravelDto cancelTravelDto) {
        log.info("[START] endpoint nvr/cancelTravel");
        
        
        return orderFacade.nvrCancelTravel(cancelTravelDto.getMotorizedId(), "OPE").map(r -> ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(r))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(r -> log.info("[END] endpoint cancelTravel "))
                .subscribeOn(Schedulers.parallel());
    }

    //old version
    @PostMapping("/delivery-travel")
    @Secured({"ROLE_MOTORIZED"})
    public void createDeliveryTravel() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }
}
