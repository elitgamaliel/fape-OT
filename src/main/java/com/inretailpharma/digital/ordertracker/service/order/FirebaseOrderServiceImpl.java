package com.inretailpharma.digital.ordertracker.service.order;

import static com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel.create;
import static com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil.FIREBASE_MOTORIZED_PATH;
import static com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil.FIREBASE_ORDER_PATH;
import static com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil.createPath;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.FIREBASE_STATUS_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.MOTORIZAD_LATLON_PATH_LISTENER;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.ORDER_STATUS_ORDER_PATH_LISTENER;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.dto.AddressLocationDto;
import com.inretailpharma.digital.ordertracker.dto.OrderStatusDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.dto.StatusMotorizedDto;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.model.AddressLocation;
import com.inretailpharma.digital.ordertracker.firebase.model.GroupCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedLatLonCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedStatusCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.OrderCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.OrderStatusCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.TimesCanonical;
import com.inretailpharma.digital.ordertracker.firebase.service.OrderTrackerFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FirebaseOrderServiceImpl implements FirebaseOrderService {

    private OrderTrackerFirebaseService orderTrackerFirebaseService;
    
    public FirebaseOrderServiceImpl(OrderTrackerFirebaseService orderTrackerFirebaseService) {
    	this.orderTrackerFirebaseService = orderTrackerFirebaseService;
    }

    @Override
    public void assignOrders(ProjectedGroupDto projectedGroupDto, String localType, OrderTrackerStatus.Code code) {
        log.info("ProjectedGroupCanonical: *** {}", projectedGroupDto.toString());
        projectedGroupDto.getGroup().forEach(canonical -> {
        	log.info("ProjectedGroupCanonical: canonical *** {}", canonical.toString());
            GroupCanonical groupCanonical = new GroupCanonical();
            groupCanonical.setName(projectedGroupDto.getGroupName());
            groupCanonical.setInProcess(Constant.Logical.N.name());

            OrderStatusCanonical assignedStatus = new OrderStatusCanonical();
            assignedStatus.setStatusName(code.name());
            assignedStatus.setStatusDate(DateUtil.currentDate().getTime());

            OrderCanonical orderCanonical = new OrderCanonical();
            orderCanonical.setOrderTrackingCode(canonical.getOrderTrackingCode());
            orderCanonical.setMotorizedId(projectedGroupDto.getMotorizedId());
            orderCanonical.setOrderStatus(assignedStatus);
            orderCanonical.setOrderExternalId(canonical.getOrderId());

            orderCanonical.setSort(canonical.getPosition());
            orderCanonical.setAddressLocation(addressLocation(canonical.getAddressLocationDto()));
            orderCanonical.setLocalType(localType);
            orderTrackerFirebaseService.save(FIREBASE_ORDER_PATH, create(String.valueOf(orderCanonical.getOrderTrackingCode()), orderCanonical));
        });

        MotorizedStatusCanonical motorizedCanonical = new MotorizedStatusCanonical();
        motorizedCanonical.setStatusName(Status.Code.PACKING.name());
        motorizedCanonical.setStatusDate(DateUtil.currentDateLong());
        orderTrackerFirebaseService.update(createPath(FIREBASE_MOTORIZED_PATH, projectedGroupDto.getMotorizedId()),
                FirebaseModel.create(FIREBASE_STATUS_PATH, motorizedCanonical));
    }

    private AddressLocation addressLocation(AddressLocationDto addressLocationDto) {
        if (addressLocationDto.getLatitude() != null && addressLocationDto.getLongitude() != null) {
            return new AddressLocation(Double.parseDouble(String.valueOf(addressLocationDto.getLatitude())),
                    Double.parseDouble(String.valueOf(addressLocationDto.getLongitude())));
        }
        return null;
    }

    @Override
    public void unassingOrders(List<String> orderTrackingCodes) {
        orderTrackingCodes.forEach(orderTrackingCode ->
            orderTrackerFirebaseService.delete(FIREBASE_ORDER_PATH, String.valueOf(orderTrackingCode))
        );
    }

    @Override
    public void updateOrdersEta(ProjectedGroupDto projectedGroupDto) {
        projectedGroupDto.getGroup().forEach(canonical -> {

            TimesCanonical timesCanonical = new TimesCanonical();
            timesCanonical.setEta(canonical.getEta().getProjected());

            orderTrackerFirebaseService.update(createPath(FIREBASE_ORDER_PATH, canonical.getOrderTrackingCode()),
                    FirebaseModel.create(FirebaseUtil.Orders.ORDER_TIMES_PATH, timesCanonical));
        });
    }

    @Override
    public void kitStatusUpdate(String motorizedId, OrderStatusDto orderStatusDto, Constant.MotorizedType currentType, String updateBy) {
        long date = System.currentTimeMillis();
        OrderStatusCanonical orderStatusCanonical = new OrderStatusCanonical();
        orderStatusCanonical.setStatusName(orderStatusDto.getStatus());
        orderStatusCanonical.setStatusDate(date);
        orderStatusCanonical.setPreviousStatus(orderStatusDto.getPreviousStatus());

        StatusMotorizedDto motorizedDto = new StatusMotorizedDto();
        motorizedDto.setStatusName(orderStatusDto.getStatus());
        motorizedDto.setStatusDate(date);

        orderTrackerFirebaseService.update(createPath(FIREBASE_ORDER_PATH, orderStatusDto.getOrderTrackingCode()),
                FirebaseModel.create(ORDER_STATUS_ORDER_PATH_LISTENER, orderStatusCanonical));

        if (motorizedId != null && orderStatusDto.getStatus().equals(OrderTrackerStatus.Code.ON_ROUTE.name())) {
            orderTrackerFirebaseService.update(createPath(FIREBASE_MOTORIZED_PATH, motorizedId),
                    FirebaseModel.create(FIREBASE_STATUS_PATH, motorizedDto));
        }

        if (orderStatusDto.getLatitude() != null && orderStatusDto.getLongitude() != null) {
            MotorizedLatLonCanonical motorizedLatLonCanonical = new MotorizedLatLonCanonical();
            motorizedLatLonCanonical.setLatitude(orderStatusDto.getLatitude());
            motorizedLatLonCanonical.setLongitude(orderStatusDto.getLongitude());

            orderTrackerFirebaseService.update(createPath(FIREBASE_MOTORIZED_PATH, motorizedId),
                    FirebaseModel.create(MOTORIZAD_LATLON_PATH_LISTENER, motorizedLatLonCanonical));
        }

        if (OrderTrackerStatus.Code.FINALIZED.contains(orderStatusDto.getStatus())) {
            orderTrackerFirebaseService.delete(FIREBASE_ORDER_PATH, String.valueOf(orderStatusDto.getOrderTrackingCode()));
        }
    }
}
