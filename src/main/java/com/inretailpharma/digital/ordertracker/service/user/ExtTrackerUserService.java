package com.inretailpharma.digital.ordertracker.service.user;

import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedTypeDto;
import com.inretailpharma.digital.ordertracker.dto.external.ExternalMotorizedStatusDto;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.exception.UserTypeException;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.service.external.tracker.ExternalTrackerFactory;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("externalTrackerUserService")
public class ExtTrackerUserService extends AbstractUserService implements UserService {

    private ExternalTrackerFactory externalTrackerFactory;
    private UserRepository userRepository;

    public ExtTrackerUserService(ExternalTrackerFactory externalTrackerFactory,
    		UserRepository userRepository) {
        this.externalTrackerFactory = externalTrackerFactory;
        this.userRepository = userRepository;
    }

    @Override
    public void updateMotorizedStatus(String motorizedId, String status) {
		log.info("[START] updateMotorizedStatus - {}, {}", motorizedId, status);
    	Constant.MotorizedType currentType = this.getMotorizedType(motorizedId);
    	if (currentType == null) {
    		log.error("Motorized type null cannot send update to the external tracker - user {}", motorizedId);
    		throw new UserTypeException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);
    	}
    	
        ExternalMotorizedStatusDto motorizedStatusDto = ExternalMotorizedStatusDto.builder()
                .statusName(status)
                .statusDate(DateUtil.currentDateLong())
                .build();

         	externalTrackerFactory
                .getService(currentType)
                .updateMotorizedStatus(motorizedId, motorizedStatusDto);
    }

    @Override
    public void updateMotorizedStatusLocation(String motorizedId, String status, MotorizedTypeDto motorizedTypeDto) {
    	
    	Constant.MotorizedType currentType = this.getMotorizedType(motorizedId);
    	if (currentType == null) {
    		log.error("Motorized type null cannot send update to the external tracker - user {}", motorizedId);
    		throw new UserTypeException(Constant.Error.ORDER_STATUS_ERROR, Constant.ErrorCode.ORDER_STATUS_ERROR);
    	}
    	
        ExternalMotorizedStatusDto motorizedStatusDto = ExternalMotorizedStatusDto.builder()
                .statusName(status)
                .statusDate(DateUtil.currentDateLong())
                .latitude(motorizedTypeDto.getLatitude())
                .longitude(motorizedTypeDto.getLongitude())
                .build();
        log.info("motorized-online ***firebase***  {}", motorizedStatusDto.toString());
        externalTrackerFactory
                .getService(currentType)
                .updateMotorizedStatus(motorizedId, motorizedStatusDto);
    }
    
    @Override
    public void updateUser(LoginDto loginDto, String motorizedId) {
		log.info("[START] updateUser - loginDto: {}, motorizedId: {}", loginDto, motorizedId);
    	if ((LoginDto.Type.LOGOUT.equals(loginDto.getType())) 
    		&& (loginDto.getCurrentMotorizedType() != null)) {
    			
            	ExternalMotorizedStatusDto offlineStatus = ExternalMotorizedStatusDto.builder()
                        .statusName(Status.Code.OFFLINE.name())
                        .statusDate(DateUtil.currentDateLong())
                        .build();
            	
    			updateStatusMotorized(motorizedId, offlineStatus, loginDto.getCurrentMotorizedType());
    		}
    	
    }
    
    @Override
	public void updateMotorizedType(String motorizedId,  MotorizedTypeDto motorizedTypeDto) {

		log.info("[START] updateMotorizedType - {}, {}", motorizedId, motorizedTypeDto);
    	Long currentDate = DateUtil.currentDateLong();
    	ExternalMotorizedStatusDto offlineStatus = ExternalMotorizedStatusDto.builder()
                .statusName(Status.Code.OFFLINE.name())
                .statusDate(currentDate)
                .build();
    	ExternalMotorizedStatusDto onlineStatus = ExternalMotorizedStatusDto.builder()
                .statusName(Status.Code.AVAILABLE.name())
                .statusDate(currentDate)
                .build();
    	
    	Constant.MotorizedType newtype = Constant.MotorizedType.parseByName(motorizedTypeDto.getType());
    	
    	Constant.MotorizedType currentType = motorizedTypeDto.getCurrentMotorizedType();
		log.info("[START] updateMotorizedType - currentType: {}", currentType);
    	if (currentType != null && !currentType.equals(newtype)) {
			updateStatusMotorized(motorizedId, offlineStatus, currentType);
		}
		updateStatusMotorized(motorizedId, onlineStatus, newtype);
	}
	@Override
	public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, String localCode, String status) {

	}

	public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, Constant.MotorizedType currentMotorizedType, String localCode, String status) {}
    
    private void updateStatusMotorized(String motorizedId, ExternalMotorizedStatusDto motorizedStatusDto, Constant.MotorizedType motorizedType) {
		log.info("[START] updateStatusMotorized");
		externalTrackerFactory
	        .getService(motorizedType) 
	        .updateMotorizedStatus(motorizedId, motorizedStatusDto);
    }
    
    private Constant.MotorizedType getMotorizedType(String motorizedId) {
    	User user = userRepository.getOne(motorizedId);
    	if (user.getType() != null) {
    		return user.getType().getCode();
    	} 
    	return null;	
    }
}
