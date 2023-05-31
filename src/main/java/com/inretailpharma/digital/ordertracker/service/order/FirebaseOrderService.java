package com.inretailpharma.digital.ordertracker.service.order;

import java.util.List;

import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.utils.Constant;

public interface FirebaseOrderService {
	
	void assignOrders(ProjectedGroupDto projectedGroupDto, String localType, OrderTrackerStatus.Code code);
    void unassingOrders(List<String> orderTrackingCodes);
    void updateOrdersEta(ProjectedGroupDto projectedGroupDto);
    void kitStatusUpdate(String motorizedId, OrderStatusDto orderStatusDto, Constant.MotorizedType currentType, String updateBy);
}
