package com.inretailpharma.digital.ordertracker.transactions;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedTypeDto;
import com.inretailpharma.digital.ordertracker.dto.ResetPasswordDto;
import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.exception.UnknownDeviceException;
import com.inretailpharma.digital.ordertracker.exception.UserException;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.Constant.MotorizedType;
import com.inretailpharma.digital.ordertracker.utils.RandomCodeGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
@Component
public class UserTransaction {
    
	private UserService trackerUserService;    
	private UserService firebaseUserService;
    private UserService extTrackerUserService;
    
    public UserTransaction(@Qualifier("trackerUserService") UserService trackerUserService,
    		@Qualifier("firebaseUserService") UserService firebaseUserService,
    		@Qualifier("externalTrackerUserService") UserService extTrackerUserService) {
    	
    	this.trackerUserService = trackerUserService;
    	this.firebaseUserService = firebaseUserService;
    	this.extTrackerUserService = extTrackerUserService;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void updatePhoneNumber(String userId, String phoneNumber) {
        log.info("[START] updatePhoneNumber - userId: {}, phoneNumber: {}", userId, phoneNumber);
        trackerUserService.updatePhoneNumber(userId, phoneNumber);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public ResponseDTO<String> updateMotorized(LoginDto loginDto, String motorizedId) {
        log.info("[START] updateMotorized - loginDto: {}, motorizedId: {}", loginDto, motorizedId);
        ResponseDTO<String> response = new ResponseDTO<>();
        try {
            log.info("updateMotorized - try()");
            response.setCode(Constant.CODE_SUCCESS);
            response.setMessage(Constant.MESSAGE_SUCCESS);
            
            User user = trackerUserService.findUser(motorizedId);
            log.info("updateMotorized - user.getType(): {}", user.getType());
            Optional.ofNullable(user.getType()).ifPresent(type -> {
                log.info("ifPresent() - type: {}", type);
            	loginDto.setCurrentMotorizedType(type.getCode());
            });
            log.info("updateMotorized - loginDto.getCurrentMotorizedType(): {}", loginDto.getCurrentMotorizedType());
            
            trackerUserService.updateUser(loginDto, motorizedId);
            extTrackerUserService.updateUser(loginDto, motorizedId);
            firebaseUserService.updateUser(loginDto, motorizedId);
            
            if (loginDto.getCurrentMotorizedType() != null && LoginDto.Type.LOGIN.equals(loginDto.getType())) {
            	
            	log.info("[INFO] updateUser - motorizedType not null {} - {}", motorizedId, loginDto);
        		MotorizedTypeDto motorizedTypeDto = new MotorizedTypeDto();
        		motorizedTypeDto.setType(loginDto.getCurrentMotorizedType().name());
        		motorizedTypeDto.setImei(loginDto.getImei());
                firebaseUserService.updateMotorizedType(motorizedId, motorizedTypeDto);

                log.info("[INFO] updateUser - user.getCurrentLocal(): {}", user.getCurrentLocal());
                Optional.ofNullable(user.getCurrentLocal()).ifPresent(localCode ->
                	firebaseUserService.updateMotorizedLocal(motorizedId, localCode, loginDto.getCurrentMotorizedType())
                );            	
            }

        } catch (UnknownDeviceException e) {
            log.info("updateMotorized - catch()");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void updateStatusMotorized(String motorizedId, String status, String localCode) {
        trackerUserService.updateMotorizedStatus(motorizedId, status, localCode);        
        extTrackerUserService.updateMotorizedStatus(motorizedId, status);
        firebaseUserService.updateMotorizedStatus(motorizedId, status);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void updateMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto) {
    	motorizedTypeDto.setCurrentMotorizedType(trackerUserService.getCurrentMotorizedType(motorizedId));
        trackerUserService.updateMotorizedType(motorizedId, motorizedTypeDto);
        extTrackerUserService.updateMotorizedType(motorizedId, motorizedTypeDto);
        firebaseUserService.updateMotorizedType(motorizedId, motorizedTypeDto);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void create(UserDto userDto) {
    	validateCreateUser(userDto);
    	trackerUserService.newUser(userDto);
    	firebaseUserService.newUser(userDto);
    }
    
    private void validateCreateUser(UserDto userDto) {
    	User userValid = trackerUserService.findByEmail(userDto.getEmail());
		if(userValid !=null) {
			throw new UserException("User with email: " + userDto.getEmail() + " exists in OrderTracker");
		}
		userValid = trackerUserService.findById(userDto.getId());
		if(userValid !=null) {
			throw new UserException("User with id " + userDto.getId() + " exists in OrderTracker");
		}

    	userDto.getRoles().stream()
    		.filter(r -> !Constant.ROLE_MOTORIZED.equals(r))
    		.findAny()
    		.ifPresent(role -> {
    			throw new UserException("Can not create a user with this role: " + role);
    		});

    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void update(UserDto userDto) {
    	trackerUserService.updateUser(userDto);
        firebaseUserService.updateUser(userDto);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void resertPassword(ResetPasswordDto resetPasswordDto) {
    	firebaseUserService.updateUserPassword(resetPasswordDto);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void changeCurrentLocal(String motorizedId, String localCode) {
    	Constant.MotorizedType currentMotorizedType = trackerUserService.getCurrentMotorizedType(motorizedId);
    	trackerUserService.updateMotorizedLocal(motorizedId, localCode, currentMotorizedType);
    	firebaseUserService.updateMotorizedLocal(motorizedId, localCode, currentMotorizedType);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void updateDeviceVersion(String motorizedId, String version) {
        trackerUserService.updateDeviceVersion(motorizedId, version);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class}, isolation = Isolation.READ_COMMITTED)
    public void updateMotorized(UpdateUserDto updateUserDto) {
        log.info("[START] updateMotorized - {}", updateUserDto);
    	MotorizedType motorizedType = updateUserDto.getLocalType();
    	String motorizedId = updateUserDto.getUserId();
    	String localCode = updateUserDto.getLocalCode();
    	String status = updateUserDto.getStatusName();
    	
    	MotorizedTypeDto motorizedTypeDto = new MotorizedTypeDto(motorizedType.name());
//    	motorizedTypeDto.setImei(RandomCodeGenerator.nextCode());
    	motorizedTypeDto.setImei(updateUserDto.getImei());

    	Constant.MotorizedType currentMotorizedType = trackerUserService.getCurrentMotorizedType(updateUserDto.getUserId());
    	motorizedTypeDto.setCurrentMotorizedType(currentMotorizedType);

        trackerUserService.updateMotorized(motorizedId, motorizedTypeDto, localCode, status);
        extTrackerUserService.updateMotorizedType(motorizedId, motorizedTypeDto);
        firebaseUserService.updateMotorized(motorizedId, motorizedTypeDto, motorizedType, localCode, status);
    }
    
    
}
