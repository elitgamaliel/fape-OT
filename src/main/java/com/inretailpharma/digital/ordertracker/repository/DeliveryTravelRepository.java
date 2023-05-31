package com.inretailpharma.digital.ordertracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;

public interface DeliveryTravelRepository extends JpaRepository<DeliveryTravel, Long> {
	
	DeliveryTravel findFirstByMotorizedIdOrderByIdDesc(String motorizedId);
	DeliveryTravel findFirstByMotorizedIdAndTravelStatusInOrderByDateCreatedDesc(String motorizedId, List<DeliveryTravel.TravelStatus> status);
	Optional<DeliveryTravel> findByGroupName(String groupName);
	Optional<DeliveryTravel> findByGroupNameAndTravelStatus(String groupName, DeliveryTravel.TravelStatus status);
	List<DeliveryTravel> findByMotorizedIdAndTravelStatusIn(String motorizedId, List<DeliveryTravel.TravelStatus> status);
	Long countByMotorizedIdAndTravelStatusIn(String motorizedId, List<DeliveryTravel.TravelStatus> status);
	DeliveryTravel findFirstByMotorizedIdAndTravelStatusIn(String motorizedId, List<DeliveryTravel.TravelStatus> status);
	List<DeliveryTravel> findByMotorizedIdAndTravelStatusNotIn(String motorizedId,List<DeliveryTravel.TravelStatus> excludedStatuses);

	boolean existsByGroupName(String groupName);
}
