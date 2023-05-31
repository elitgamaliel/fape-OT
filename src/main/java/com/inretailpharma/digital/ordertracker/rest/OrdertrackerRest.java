package com.inretailpharma.digital.ordertracker.rest;

import java.util.List;
import java.util.Optional;

import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.exception.OrderException;
import lombok.extern.slf4j.Slf4j;
import com.inretailpharma.digital.ordertracker.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.OrderDto;
import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.PageDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.TrackerReasonDto;
import com.inretailpharma.digital.ordertracker.dto.TravelDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.TrackerReason;
import com.inretailpharma.digital.ordertracker.facade.OrderFacade;
import com.inretailpharma.digital.ordertracker.service.order.FirebaseOrderServiceImpl;
import com.inretailpharma.digital.ordertracker.service.order.OrdertrackerServiceImpl;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import io.swagger.annotations.Api;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@Api(value = "OrdertrackerRest", produces = "application/json")
public class OrdertrackerRest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private OrdertrackerServiceImpl ordertrackerService;

    @Autowired
    FirebaseOrderServiceImpl firebaseOrderService;

    @PostMapping("/order")
    public void save(@RequestBody OrderDto orderDto) {
        orderFacade.save(orderDto);
    }

    @GetMapping("/orders/status")
    public Flux<OrderDto> findOrdersByStatus(Pageable pageable, @RequestParam Long drugstore,
                                             @RequestParam(required = false)
                                                     List<OrderTrackerStatus.Code> status) {
        return ordertrackerService.findOrders(pageable, drugstore, status);
    }

    @GetMapping("/order/search-order")
    public OrderDto findOrderByOrderId(@RequestParam String orderTrackingCode) {
        return ordertrackerService.findOrderTrackingCode(orderTrackingCode);
    }

    @PatchMapping("/v2/order/status/location")
    @Secured({"ROLE_MOTORIZED"})
    public Mono<ResponseEntity<ResponseDTO<String>>> changeStatusOrderLocation(@RequestBody OrderStatusDto orderStatusDto) {
        log.info("[START] endpoint PATCH //order/status/location{}", orderStatusDto);
        return orderFacade.
        		statusUpdateLocation(SecurityUtils.getUID(), orderStatusDto, SecurityUtils.getUID())
                .map(r -> ResponseEntity
                            .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                            .body(new ResponseDTO<String>(Constant.CODE_SUCCESS, null, r)))
                .subscribeOn(Schedulers.parallel());
    }

    @GetMapping("/orders/{type}/reason")
    public List<TrackerReasonDto> transferReason(@PathVariable String type) {
        TrackerReason.Type reasonType = TrackerReason.Type.valueOf(type.toUpperCase());
        return orderFacade.getAllTransferReason(reasonType);
    }

    @GetMapping("/v2/orders/local/{localCode}/{page}")
    public PageDto<OrderDto> findOrdersByLocal(
    		@PathVariable("localCode") String localCode,
    		@PathVariable("page") Integer page) {
        return ordertrackerService.findOrdersByLocal(localCode, page, null);
    }
    
    @GetMapping("/v2/orders/local/{localCode}/{page}/{query}")
    public PageDto<OrderDto> findOrdersByLocalQuery(
    		@PathVariable("localCode") String localCode,
    		@PathVariable("page") Integer page,
    		@PathVariable("query") Optional<String> query) {
        return ordertrackerService.findOrdersByLocal(localCode, page, query.orElse(null));
    }
    
    @PostMapping("/orders/group")
    public ProjectedGroupDto helpGroupOrder(@RequestBody TravelDto travelDto) {
    	ProjectedGroupDto response = orderFacade.helpGroupOrder(travelDto);
    	response.setGroupName(null);
    	response.setPickUpCenter(null);
    	return response;
    }

    @PostMapping("orders/audit/distance")
    @Secured({"ROLE_MOTORIZED"})
    public Mono<ResponseEntity<String>> saveAudit(@RequestBody OrderDistanceAuditDto orderDistanceAuditDto){
        return orderFacade.
                saveAuditDistance(SecurityUtils.getUID(), orderDistanceAuditDto)
                .map(r -> ResponseEntity
                        .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(r))
                .subscribeOn(Schedulers.parallel());
    }

    //old version
    @PatchMapping("/order/status/location")
    @Secured({"ROLE_MOTORIZED"})
    public void changeStatusOrderLocation() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }

    @GetMapping("/orders/local/{localCode}/{page}")
    public void findOrdersByLocal() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }

    @GetMapping("/orders/local/{localCode}/{page}/{query}")
    public void findOrdersByLocalQuery() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }
}

