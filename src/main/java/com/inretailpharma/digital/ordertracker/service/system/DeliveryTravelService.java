package com.inretailpharma.digital.ordertracker.service.system;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.dto.RouteDto;
import com.inretailpharma.digital.ordertracker.mapper.DeliveryTravelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.inretailpharma.digital.ordertracker.dto.GroupDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravelDetail;
import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.entity.OrderTrackerStatus;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.exception.DeliveryTravelException;
import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.repository.DeliveryTravelRepository;
import com.inretailpharma.digital.ordertracker.repository.OrdertrackerRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryTravelService {

    private DeliveryTravelRepository deliveryTravelRepository;
    private OrdertrackerRepository ordertrackerRepository;
    private UserRepository userRepository;
    private UserService trackerUserService;
    private UserService firebaseUserService;
    private UserService extTrackerUserService;

    public DeliveryTravelService(final DeliveryTravelRepository deliveryTravelRepository
            , OrdertrackerRepository ordertrackerRepository
            , UserRepository userRepository
            , @Qualifier("trackerUserService") UserService trackerUserService
            , @Qualifier("firebaseUserService") UserService firebaseUserService
            , @Qualifier("externalTrackerUserService") UserService extTrackerUserService) {
        this.deliveryTravelRepository = deliveryTravelRepository;
        this.ordertrackerRepository = ordertrackerRepository;
        this.userRepository = userRepository;
        this.trackerUserService = trackerUserService;
        this.firebaseUserService = firebaseUserService;
        this.extTrackerUserService = extTrackerUserService;
    }

    public DeliveryTravel findLastDeliveryTravel(String motorizedId) {
        return deliveryTravelRepository.findFirstByMotorizedIdOrderByIdDesc(motorizedId);
    }

    public DeliveryTravel markLastDeliveryTravelAsFinalized(String motorizedId) {
        Optional<DeliveryTravel> travel =
                Optional.ofNullable(deliveryTravelRepository
                        .findFirstByMotorizedIdAndTravelStatusInOrderByDateCreatedDesc(motorizedId
                                , Arrays.asList(DeliveryTravel.TravelStatus.IN_PROGRESS)));

        travel.ifPresent(deliveryTravel -> {
            deliveryTravel.setEndDate(DateUtil.currentDate());
            deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.FINALIZED);
            log.info("Delivery travel is about to be finalized: " + deliveryTravel.getGroupName());
            deliveryTravelRepository.save(deliveryTravel);
        });

        return travel.orElse(null);
    }

    public void createDeliveryTravel(ProjectedGroupDto projectedGroupDto, Map<Long, OrderTracker> ordersMap, OrderTrackerStatus.Code code) {
        
        DeliveryTravel deliveryTravel = new DeliveryTravel();
        deliveryTravel.setGroupName(projectedGroupDto.getGroupName());
        deliveryTravel.setMotorizedId(projectedGroupDto.getMotorizedId());
        deliveryTravel.setDateCreated(DateUtil.currentDate());
        deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.CREATED);        
        deliveryTravel.setProjectedEta(projectedGroupDto.getProjectedEtaReturn());
        
        User motorized = userRepository.findById(projectedGroupDto.getMotorizedId()).orElse(null);
        
        OrderTrackerStatus orderStatus = new OrderTrackerStatus();
        orderStatus.setCode(code);
        
        LocalDateTime now = LocalDateTime.now();       
        deliveryTravel.setDetailList(projectedGroupDto.getGroup().stream()
        	.filter(f -> {
        		boolean result = ordersMap.containsKey(f.getOrderId());
        		if (!result) {
        			log.info(">>>createDeliveryTravel - Order {} not found", f.getOrderId());
        		}
        		return result;
        	}).map(groupDto -> {
        		
        		OrderTracker order = ordersMap.get(groupDto.getOrderId());
        		
        		groupDto.setOrderTrackingCode(order.getOrderTrackingCode());
        		
        		
        		order.setGroupOrder(groupDto.getPosition());
                order.setGroupName(projectedGroupDto.getGroupName());
                order.setMotorized(motorized);
                order.setOrderStatusDate(now);
                order.setOrderTrackerStatus(orderStatus);

                DeliveryTravelDetail detail = new DeliveryTravelDetail(order);
                detail.setOrderGroup(groupDto.getPosition());
                detail.setProjectedEta(groupDto.getEta().getProjected());
        		return detail;        		
        		
        	}).collect(Collectors.toList())
        );

        Optional.ofNullable(projectedGroupDto.getPickUpCenter()).ifPresent(pickUpCenter -> {
            deliveryTravel.setPickUpCenterName(pickUpCenter.getName());
            deliveryTravel.setPickUpCenterLatitude(pickUpCenter.getLatitude());
            deliveryTravel.setPickUpCenterLongitude(pickUpCenter.getLongitude());
        });

        deliveryTravelRepository.save(deliveryTravel);
    }

    public void cancelDeliveryTravel(String groupName) {
        Optional<DeliveryTravel> deliveryTravelInfo = deliveryTravelRepository.findByGroupName(groupName);
        deliveryTravelInfo.ifPresent(deliveryTravel -> {
            deliveryTravel.setEndDate(DateUtil.currentDate());
            deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.CANCELLED);
            deliveryTravelRepository.save(deliveryTravel);
        });
    }

    public List<DeliveryTravel> findDeliveryTravelNotFinalized(String motorizedId) {
        List<DeliveryTravel.TravelStatus> travelStatusNotFinalized =
                Arrays.asList(DeliveryTravel.TravelStatus.CREATED, DeliveryTravel.TravelStatus.IN_PROGRESS);        
        return deliveryTravelRepository.findByMotorizedIdAndTravelStatusIn(motorizedId, travelStatusNotFinalized);
    }

    public void checkIfDeliveryTravelIsFinalizedByMotorized(String motorizedId) {
        Optional<DeliveryTravel> lastDeliveryTravel =
                Optional.ofNullable(deliveryTravelRepository.findFirstByMotorizedIdOrderByIdDesc(motorizedId));
        this.checkIfDeliveryTravelIsFinalized(lastDeliveryTravel, motorizedId);
    }
    
    public void checkIfDeliveryTravelIsFinalizedByGroup(String groupName, String motorizedId) {
        Optional<DeliveryTravel> deliveryTravel = deliveryTravelRepository.findByGroupName(groupName);
        this.checkIfDeliveryTravelIsFinalized(deliveryTravel, motorizedId);
    }

    public Optional<DeliveryTravel> findDeliveryTravelByGroupName(String groupName) {
        return deliveryTravelRepository.findByGroupName(groupName);
    }

    public void updateDeliveryTravelEta(DeliveryTravel deliveryTravel, List<OrderTracker> orderList, List<GroupDto> group) {
        deliveryTravel.getDetailList().clear();
        AtomicInteger index = new AtomicInteger(0);
        orderList.forEach(order -> {
            GroupDto groupDto = group.get(index.getAndIncrement());
            log.info("# Update TravelDetail: {} for order: {}", groupDto, order.getEcommercePurchaseId());
            DeliveryTravelDetail travelDetail = new DeliveryTravelDetail(order);
            travelDetail.setOrderGroup(groupDto.getPosition());
            deliveryTravel.getDetailList().add(travelDetail);

            log.info("# Update Order: {} in new position: {}", order.getEcommercePurchaseId(), groupDto.getPosition());
            order.setGroupOrder(groupDto.getPosition());
            ordertrackerRepository.save(order);
        });
        deliveryTravelRepository.save(deliveryTravel);
    }
    
    public DeliveryTravel cancelDeliveryTravelForMotorized(String motorizedId) {
        DeliveryTravel deliveryTravel = deliveryTravelRepository.findFirstByMotorizedIdAndTravelStatusIn(motorizedId, Collections.singletonList(DeliveryTravel.TravelStatus.CREATED));

        if (deliveryTravel == null) {
            log.error("Cannot cancel travel for motorized with id {} ", motorizedId);
            throw new InvalidRequestException(Constant.Error.CANCEL_TRAVEL_ERROR);
        }
        deliveryTravel.setEndDate(DateUtil.currentDate());
        deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.CANCELLED);
        deliveryTravelRepository.save(deliveryTravel);
        log.info("# Cancel DeliveryTravel {}", deliveryTravel.getId());
        return deliveryTravel;
    }
    
    public void startTravel(String groupName) {
    	Optional<DeliveryTravel> deliveryTravelInfo = deliveryTravelRepository.findByGroupNameAndTravelStatus(groupName, DeliveryTravel.TravelStatus.CREATED);
        deliveryTravelInfo.ifPresent(deliveryTravel -> {
        	if (ObjectUtils.isEmpty(deliveryTravel.getStartDate())) {
        		deliveryTravel.setStartDate(DateUtil.currentDate());
                deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.IN_PROGRESS);
                deliveryTravelRepository.save(deliveryTravel);
        	}
        });
    }
    
    public void validateNewGroupName(String groupName) {
    	if (deliveryTravelRepository.existsByGroupName(groupName)) {
    		throw new DeliveryTravelException(Constant.Error.TRAVEL_EXISTS_ERROR);
    	}
    }
    
    private void checkIfDeliveryTravelIsFinalized(Optional<DeliveryTravel> optDeliveryTravel, String motorizedId) {
    	optDeliveryTravel.ifPresent(deliveryTravel -> {
            log.info("#Checking if travel {} is finalized.", deliveryTravel.getGroupName());

            if (!Arrays.asList(DeliveryTravel.TravelStatus.CANCELLED, DeliveryTravel.TravelStatus.FINALIZED)
                    .contains(deliveryTravel.getTravelStatus())) {

                List<String> orderStatusFinalized = OrderTrackerStatus.Code.FINALIZED.getChildrenNames();

                Long ordersNotFinalized =
                        ordertrackerRepository.countByIdTravelDeliveryAndOrderTrackerStatusNotIn(deliveryTravel.getId(),
                                orderStatusFinalized);

                log.info("#Travel has {} orders remaining.", ordersNotFinalized);
                if (ordersNotFinalized == 0) {
                    deliveryTravel.setEndDate(DateUtil.currentDate());
                    deliveryTravel.setTravelStatus(DeliveryTravel.TravelStatus.FINALIZED);
                    deliveryTravelRepository.save(deliveryTravel);
                    String status = Status.Code.RETURNING.name();
                    trackerUserService.updateMotorizedStatus(motorizedId, status);                    
                    extTrackerUserService.updateMotorizedStatus(motorizedId, status);
                    firebaseUserService.updateMotorizedStatus(motorizedId, status);
                }
            }
        });
    }

    public String getGroupNameOfCurrentTravel(String motorizedId){
        Optional<DeliveryTravel> travel =
                Optional.ofNullable(deliveryTravelRepository
                        .findFirstByMotorizedIdAndTravelStatusInOrderByDateCreatedDesc(motorizedId
                                , Arrays.asList(DeliveryTravel.TravelStatus.CREATED, DeliveryTravel.TravelStatus.IN_PROGRESS)));

        return travel.orElse(new DeliveryTravel()).getGroupName();
    }

    public List<RouteDto> getAssignedRoutes(String motorizedId) {
        List<DeliveryTravel.TravelStatus> excludedStatuses =
                Arrays.asList(DeliveryTravel.TravelStatus.CANCELLED, DeliveryTravel.TravelStatus.FINALIZED);
        return DeliveryTravelMapper.convertDeliveryTravelToRouteDto(deliveryTravelRepository.findByMotorizedIdAndTravelStatusNotIn(
                motorizedId, excludedStatuses));
    }
}
