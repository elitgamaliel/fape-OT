package com.inretailpharma.digital.ordertracker.service.system;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.dto.DrugstoreDto;
import com.inretailpharma.digital.ordertracker.dto.EtaDto;
import com.inretailpharma.digital.ordertracker.dto.GroupDto;
import com.inretailpharma.digital.ordertracker.dto.OrderPositionDto;
import com.inretailpharma.digital.ordertracker.dto.PickUpCenterDto;
import com.inretailpharma.digital.ordertracker.dto.ProjectedGroupDto;
import com.inretailpharma.digital.ordertracker.dto.TravelDto;
import com.inretailpharma.digital.ordertracker.entity.AddressTracker;
import com.inretailpharma.digital.ordertracker.entity.ApplicationParameter;
import com.inretailpharma.digital.ordertracker.entity.OrderTracker;
import com.inretailpharma.digital.ordertracker.exception.InvalidRequestException;
import com.inretailpharma.digital.ordertracker.repository.OrdertrackerRepository;
import com.inretailpharma.digital.ordertracker.repository.ParameterRepository;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import com.inretailpharma.digital.ordertracker.utils.ObjectUnique;
import com.inretailpharma.digital.ordertracker.utils.SpaceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProjectedEtaService {

    private ParameterRepository parameterRepository;
    private OrdertrackerRepository ordertrackerRepository;
    private ExternalService externalService;

    public ProjectedEtaService(
            final ParameterRepository parameterRepository,
            final OrdertrackerRepository ordertrackerRepository,
            final ExternalService externalService) {
        this.parameterRepository = parameterRepository;
        this.ordertrackerRepository = ordertrackerRepository;
        this.externalService = externalService;
    }

    public ProjectedGroupDto projectedEtaInOrders(List<OrderTracker> orderList, BigDecimal pickUpCenterLatitude, BigDecimal pickUpCenterLongitude) {

        List<GroupDto> groupDtoList = new ArrayList<>();

        ApplicationParameter customerDelayTime = parameterRepository.getOne(Constant.CUSTOMER_DELAY_TIME);
        ApplicationParameter travelSpeed = parameterRepository.getOne(Constant.TRAVEL_SPEED);

        SpaceUtils.Point drugstorePoint = new SpaceUtils.Point(pickUpCenterLatitude, pickUpCenterLongitude);

        ObjectUnique<SpaceUtils.Point> firstPoint = new ObjectUnique<>(drugstorePoint);
        AtomicInteger projectedEtaReturning = new AtomicInteger(BigDecimal.ZERO.intValue());
        AtomicInteger index = new AtomicInteger(1);

        orderList.forEach(order -> {
            GroupDto groupDto = new GroupDto(index.getAndIncrement(), order.getEcommercePurchaseId(), order.getOrderTrackingCode());
            calculateNextEta(groupDto, order, firstPoint, travelSpeed, customerDelayTime, projectedEtaReturning);
            groupDtoList.add(groupDto);
        });

        OrderTracker lastOrder = orderList.get(orderList.size() - BigDecimal.ONE.intValue());
        firstPoint.set(drugstorePoint);
        projectedEtaReturning.addAndGet(calculateProjectedEta(travelSpeed.getIntValue(), lastOrder, firstPoint));

        return new ProjectedGroupDto(projectedEtaReturning.get(), groupDtoList);
    }
    
    public ProjectedGroupDto projectedEtaInTravel(TravelDto travelDto) {

        List<GroupDto> groupDtoList = new ArrayList<>();
        DrugstoreDto drugstoreDto = externalService.findDrugstore(travelDto.getLocalCode());
    	if(drugstoreDto==null) {
    		log.error("no se encontro drugstore segun localCode: {}",travelDto.getLocalCode());
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
    	}
    	
    	log.info("INI - Ordering orders, size: {}",travelDto.getOrders().size());
        List<OrderPositionDto> orderPositionList = sortOrdersByRoute(drugstoreDto, travelDto.getOrders());
        log.info("FIN - Ordered orders, size: {}",orderPositionList.size());
        
        orderPositionList.forEach(orderPosition -> {
            OrderTracker order = ordertrackerRepository.findByOrderTrackingCode(orderPosition.getTrackingCode());
            GroupDto groupDto = new GroupDto(orderPosition.getPosition(), order.getEcommercePurchaseId(), order.getOrderTrackingCode());
            
            EtaDto eta = new EtaDto(10, 10);            
            groupDto.setEta(eta);            
            groupDtoList.add(groupDto);
        });


        PickUpCenterDto pickUpCenter = new PickUpCenterDto();
        pickUpCenter.setId(drugstoreDto.getLegacyId());
        pickUpCenter.setLocalCode(travelDto.getLocalCode());
        pickUpCenter.setName(drugstoreDto.getName());
        pickUpCenter.setLatitude(drugstoreDto.getLatitude());
        pickUpCenter.setLongitude(drugstoreDto.getLongitude());

        return new ProjectedGroupDto(100, groupDtoList, pickUpCenter);
    }
    
    private List<OrderPositionDto> sortOrdersByRoute(DrugstoreDto drugstoreDto, List<String> orders) {
    	log.info("sortOrdersByRoute");    	
    	//primer origen sale del localCode
    	log.info("primer origen sale del localCode: {}",drugstoreDto.getDescription());
    	OrderPositionDto orderPositionOrigin = new OrderPositionDto(null,0,drugstoreDto.getLatitude(),drugstoreDto.getLongitude(), 0D, 0, 0L);
    	if(orderPositionOrigin.getLatitude()==null || orderPositionOrigin.getLongitude()==null) {
    		log.error("Uno de los datos de ubicacion del localCode (latitude o longitude) no existen en order {}",orderPositionOrigin.getTrackingCode());
            throw new InvalidRequestException(Constant.Error.ASSIGN_ORDERS_ERROR);
    	}
    	//lista de ordenes con latlon de cada una
    	List<OrderPositionDto> orderPositionListCompare = new ArrayList<>();
    	for(String orderTrackingCode: orders) {
        	OrderTracker order = ordertrackerRepository.findByOrderTrackingCode(orderTrackingCode);
        	Constant.ServiceType serviceType = Constant.ServiceType.parseByName(order.getOrderTrackerDetail().getServiceTypeCode());        	
        	log.info("Orden encontrada: {} - servicio {} - prioridad {}",order.getEcommercePurchaseId(), order.getOrderTrackerDetail().getServiceTypeCode(), serviceType.getPriority());
            OrderPositionDto orderPositionDto = new OrderPositionDto(orderTrackingCode,null,order.getAddressTracker().getLatitude()
            		,order.getAddressTracker().getLongitude(), 0D, serviceType.getPriority(), DateUtil.convertDatetimeToMilliseconds(order.getScheduled().getEndDate()));
            orderPositionListCompare.add(orderPositionDto);
        }
    	log.info("lista de ordenes con latlon de cada una: data{}",orderPositionListCompare.toString());
        
        List<OrderPositionDto> sortedOrderList = new ArrayList<>();
        
        if(validateLatLonOrderPosition(orderPositionListCompare)) {
        	//se ordena comparando el origen(orderPositionOrigin) vs listaCompare(orderPositionListCompare)
            for(int j=0;j<orders.size();j++) {
            	//se coloca distance respecto al origin, a cada elemento de la lista
            	for(int i=0;i<orderPositionListCompare.size();i++) {
            		Double distance = SpaceUtils.distanceBetweenPoints(
            				new SpaceUtils.Point(orderPositionOrigin.getLatitude(), orderPositionOrigin.getLongitude()), 
            				new SpaceUtils.Point(orderPositionListCompare.get(i).getLatitude(), orderPositionListCompare.get(i).getLongitude()));
            		orderPositionListCompare.get(i).setDistance(distance);
                }
            	//ordenando segun distance
            	orderPositionListCompare = orderPositionListCompare.stream()
            				.sorted(Comparator
            					.comparingInt(OrderPositionDto::getPriority)
            					.thenComparingLong(OrderPositionDto::getDeliveryTime)
            					.thenComparingDouble(OrderPositionDto::getDistance))
                			.collect(Collectors.toList());
            	//agregando position al primer elemento de la lista ordenada por distance
            	orderPositionListCompare.get(0).setPosition(j+1);
            	//agregando el pedido mas cercano a lista ordenada
            	sortedOrderList.add(orderPositionListCompare.get(0));
            	//establecer nuevo origin pa sgte iteracion
            	orderPositionOrigin = orderPositionListCompare.get(0);
            	//eliminar elemento del list pa sgte iteracion
            	orderPositionListCompare.remove(0);
            }
        }else {
        	log.info("Alguna orden no tuvo los datos latitude y longitude, se ordenara segun orden de llegada en list.");
        	for(int j=0;j<orderPositionListCompare.size();j++) {
        		orderPositionListCompare.get(j).setPosition(j+1);
        	}
        	sortedOrderList=orderPositionListCompare;
        }
        
		return sortedOrderList;
    }
    
    private boolean validateLatLonOrderPosition(List<OrderPositionDto> orderPositionList) {
    	boolean res = true;
    	for(OrderPositionDto orderPosition: orderPositionList) {
    		if(orderPosition.getLatitude()==null || orderPosition.getLongitude()==null) {
    			log.info("Uno de los datos de ubicacion (latitude o longitude) no existen en order {}",orderPosition.getTrackingCode(),", se ordenanran pedidos segun llegada en lista");
                res=false;
    			break;
    		}
    	}
    	return res;
    }

    private void calculateNextEta(GroupDto groupDto, OrderTracker order, ObjectUnique<SpaceUtils.Point> firstPoint,
                                  ApplicationParameter travelSpeed, ApplicationParameter customerDelayTime, AtomicInteger projectedEtaReturning) {
        int minutes = calculateProjectedEta(travelSpeed.getIntValue(), order, firstPoint);
        projectedEtaReturning.addAndGet(minutes);
        groupDto.setEta(new EtaDto(minutes, projectedEtaReturning.get()));
        LocalDateTime future = LocalDateTime.now().plusMinutes(projectedEtaReturning.get());
        groupDto.setTimeRemaining(ChronoUnit.MINUTES.between(future, order.getScheduled().getEndDate()));
        projectedEtaReturning.addAndGet(customerDelayTime.getIntValue());
    }

    private int calculateProjectedEta(int travelSpeed, OrderTracker order, ObjectUnique<SpaceUtils.Point> secondPoint) {
        AddressTracker lastAddress = order.getAddressTracker();
        SpaceUtils.Point firstPoint = new SpaceUtils.Point(lastAddress.getLatitude(), lastAddress.getLongitude());
        Double distance1 = SpaceUtils.distanceBetweenPoints(firstPoint, secondPoint.get());
        secondPoint.set(firstPoint);
        return BigDecimal.valueOf(travelSpeed * distance1 / 1000).intValue();
    }
}
