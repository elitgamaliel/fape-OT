package com.inretailpharma.digital.ordertracker.service.external.tracker;

import static com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil.FIREBASE_MOTORIZED_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.FIREBASE_STATUS_PATH;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.inretailpharma.digital.ordertracker.config.ExternalTrackerConfig;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalMotorizedStatusDto;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.service.InkaTrackerLiteFirebaseService;
import com.inretailpharma.digital.ordertracker.mapper.ExternalMapper;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("inkatrackerlite")
public class InkaTrackerLiteService extends BaseExternalTrackerService implements ExternalTrackerService  {

    private InkaTrackerLiteFirebaseService inkaTrackerLiteFirebaseService;

	public InkaTrackerLiteService(ExternalTrackerConfig externalTrackerConfig,
			@Qualifier("externalRestTemplate") RestTemplate restTemplate,
			ExternalMapper mapper,
			InkaTrackerLiteFirebaseService inkaTrackerLiteFirebaseService) {
		
		super(externalTrackerConfig, restTemplate, mapper);
		this.inkaTrackerLiteFirebaseService = inkaTrackerLiteFirebaseService;
	}
	
	@Override
	public Constant.MotorizedType getMotorizedType() {
		return Constant.MotorizedType.DRUGSTORE;
	}

	@Override
	public void updateMotorizedStatus(String motorizedId, ExternalMotorizedStatusDto motorizedStatusDto) {
		log.info("[START] call to service - updateMotorizedStatus - body:{}", motorizedStatusDto);
		Status.Code code = Status.Code.parse(motorizedStatusDto.getStatusName());
		
		if (Status.Code.OFFLINE.equals(code)) {
			inkaTrackerLiteFirebaseService.delete(FIREBASE_MOTORIZED_PATH, motorizedId);
		} else {
			inkaTrackerLiteFirebaseService.update(FIREBASE_MOTORIZED_PATH + "/" + motorizedId,
                    FirebaseModel.create(FIREBASE_STATUS_PATH, motorizedStatusDto));
		}		
	}


	
}
