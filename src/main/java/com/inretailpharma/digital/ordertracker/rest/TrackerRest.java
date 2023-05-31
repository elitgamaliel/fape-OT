package com.inretailpharma.digital.ordertracker.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inretailpharma.digital.ordertracker.canonical.tracker.MotorizedCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderDetailCanonical;
import com.inretailpharma.digital.ordertracker.canonical.tracker.OrderMotorizedCanonical;
import com.inretailpharma.digital.ordertracker.service.order.OrdertrackerService;

@RestController
@RequestMapping("/nvr/tracker")
public class TrackerRest {
		
	@Autowired
    private OrdertrackerService ordertrackerService;

	@GetMapping(value="/motorized/{motorizedId}/orders",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<OrderMotorizedCanonical> getOrderMotorized(@PathVariable(name = "motorizedId") String motorizedId) {
		return ordertrackerService.findOrderTrackerByMotorizedId(motorizedId);
	}

	@GetMapping(value="/motorized/local/code/{localCode}",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<MotorizedCanonical> getMotorized(@PathVariable Integer localCode) {
		return ordertrackerService.findMotorizedByLocalCode(localCode);
	}

	/***obtener el detalle del pedido****/
	@GetMapping(value="/motorized/order/{orderNumber}",produces=MediaType.APPLICATION_JSON_VALUE)
	public OrderDetailCanonical getOrderDetail(@PathVariable Long orderNumber) {
		return ordertrackerService.findOrderTrackerByEcommerceId(orderNumber);
	}
}
