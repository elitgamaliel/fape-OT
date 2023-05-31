package com.inretailpharma.digital.ordertracker.rest;

import com.inretailpharma.digital.ordertracker.dto.*;

import java.util.List;

import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.inretailpharma.digital.ordertracker.facade.MotorizedFacade;
import com.inretailpharma.digital.ordertracker.facade.UserFacade;

@Slf4j
@RestController
public class MotorizedRest {

    @Autowired
    private MotorizedFacade motorizedFacade;

    @Autowired
    private UserFacade userFacade;

    @GetMapping("/motorized/travel/cancelled-orders")
    @Secured({"ROLE_MOTORIZED"})
    public List<OrderHeaderDto> getCancelledOrdersOfLastTravel() {
        return motorizedFacade.getCancelledOrdersOfLastTravel(SecurityUtils.getUID());
    }

    @GetMapping("/motorized/resume/return")
    public ReturnResumeDto getReturnResume() {
        return motorizedFacade.getReturnResume(SecurityUtils.getUID());
    }

    @GetMapping("/motorized/assigned-orders")
    @Secured({"ROLE_MOTORIZED"})
    public List<OrderDto> getAssignedOrders() {
        return motorizedFacade.getAssignedOrders(SecurityUtils.getUID());
    }

    @GetMapping("/motorized/current-travel")
    @Secured({"ROLE_MOTORIZED"})
    public List<OrderDto> getOrdersCurrentTravel() {
        return motorizedFacade.getOrdersCurrentTravel(SecurityUtils.getUID());
    }

    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/motorized/status/{status}")
    public void changeStatusOrder(@PathVariable(name = "status") String status) {
        userFacade.changeStatus(SecurityUtils.getUID(), status);
    }

    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/v2/motorized/type")
    public void changeType(@RequestBody MotorizedTypeDto motorizedTypeDto) {
        userFacade.changeMotorizedType(SecurityUtils.getUID(), motorizedTypeDto);
    }
    
    @Secured({"ROLE_MOTORIZED"})
    @GetMapping("/motorized/cancelled-orders-today")
    public List<OrderCancelledDto> findCancelledOrders() {
    	return motorizedFacade.getCancelledOrdersHistorical(SecurityUtils.getUID());
    }
    
    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/v2/motorized/local/{localCode}")
    public void changeCurrentLocal(@PathVariable("localCode") String localCode) {
        userFacade.changeCurrentLocal(SecurityUtils.getUID(), localCode);
    }
    
    @PostMapping("/nvr/motorized/export")
    public List<UserDto> export(@RequestBody List<Long> orderIds) {
        return userFacade.getMotorizedByOrders(orderIds);
    }


    @GetMapping("/motorized/detail/{ecommerceId}")
    public MotorizedDetailDto getCancelledOrdersOfLastTravel(@PathVariable("ecommerceId") Long ecommerceId) {
        return motorizedFacade.getMotorizedInfoByEcommerceId(ecommerceId);
    }

    @GetMapping("/motorized/info")
    public List<MotorizedDetailDto> getInfoMotorized(@RequestParam("ids") List<Long> listEcommerceId) {
        log.info("===entro {motorized/info}: " );
        log.info("show me list " + listEcommerceId.toString());
        List<MotorizedDetailDto> list=motorizedFacade.listInfoMotorized(listEcommerceId);
        return list;
    }

    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("motorized/device/{version}")
    public void updateDeviceVersion(@PathVariable("version") String version) {
        userFacade.updateDeviceVersion(SecurityUtils.getUID(), version);
    }
    
    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/motorized/action/{actionName}")
    public Mono<ResponseEntity<ResponseDTO<UpdateUserResponseDto>>> processUserAction(
    		@PathVariable("actionName") String actionName,
    		@RequestBody(required = false) UpdateUserDto dto) {
    	
    	if (dto == null) { dto = new UpdateUserDto(); }    	
    	dto.setUserId(SecurityUtils.getUID());
    	dto.setAction(actionName);
        log.info("[START] endpoint PATCH /motorized/action", dto);
        return userFacade.processUserAction(dto)
                .map(r -> ResponseEntity
                            .status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                            .body(r))
                .subscribeOn(Schedulers.parallel());
    }
    
    @Secured({"ROLE_MOTORIZED"})
    @GetMapping("/motorized/history")
    public ResponseDTO<List<UserHistoryDto>> getHistory(){    	
    	return userFacade.getAvailabilityHistory(SecurityUtils.getUID());
    }

    //old version
    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/motorized/type")
    public void changeType() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }

    @Secured({"ROLE_MOTORIZED"})
    @PatchMapping("/motorized/local/{localCode}")
    public void changeCurrentLocal() {
        throw new InvalidRequestException(Constant.Error.VERSION_ERROR);
    }

    @GetMapping("/motorized/assigned-routes")
    @Secured({"ROLE_MOTORIZED"})
    public List<RouteDto> getAssignedRoute() {
        return motorizedFacade.getAssignedRoutes(SecurityUtils.getUID());
    }
}
