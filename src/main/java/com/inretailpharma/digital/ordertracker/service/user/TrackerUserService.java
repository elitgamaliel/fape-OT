package com.inretailpharma.digital.ordertracker.service.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedTypeDto;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.dto.factory.CanonicalFactory;
import com.inretailpharma.digital.ordertracker.entity.Device;
import com.inretailpharma.digital.ordertracker.entity.DeviceHistory;
import com.inretailpharma.digital.ordertracker.entity.MotorizedType;
import com.inretailpharma.digital.ordertracker.entity.Role;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.TrackingStatus;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.UserStatus;
import com.inretailpharma.digital.ordertracker.entity.UserStatusHistory;
import com.inretailpharma.digital.ordertracker.entity.projection.UserHistoryProjection;
import com.inretailpharma.digital.ordertracker.entity.projection.UserReportProjection;
import com.inretailpharma.digital.ordertracker.exception.OrderTrackerException;
import com.inretailpharma.digital.ordertracker.repository.device.DeviceRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserStatusHistoryRepository;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

@Slf4j
@Service("trackerUserService")
public class TrackerUserService extends AbstractUserService implements UserService {

	private UserRepository userRepository;
	private DeviceRepository deviceRepository;
	private UserStatusHistoryRepository userStatusHistoryRepository;
    
	@Autowired
    public TrackerUserService(UserRepository userRepository,
    		DeviceRepository deviceRepository,
    		UserStatusHistoryRepository userStatusHistoryRepository) {
    	this.userRepository = userRepository;
    	this.deviceRepository = deviceRepository;
    	this.userStatusHistoryRepository = userStatusHistoryRepository;
    }

    @Override
    public List<UserStatusHistory> findLastStatusHistoryMap(User user) {
        return userRepository.findLastStatusHistoryMap(user.getId());
    }

    @Override
    public UserStatusHistory findLastStatusHistory(String userId, TrackingStatus status) {
        return userRepository.findLastStatusHistory(userId, status).findFirst().orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findValidMobileUser(String userId) throws OrderTrackerException {
        User user = this.findValidUser(userId);
        boolean isMotorized = user.getRoleList().stream().map(Role::getCode).anyMatch(Role.Code.ROLE_MOTORIZED::equals);
        boolean isPicker = user.getRoleList().stream().map(Role::getCode).anyMatch(Role.Code.ROLE_PICKER::equals);
        boolean isDispatcher = user.getRoleList().stream().map(Role::getCode).anyMatch(Role.Code.ROLE_DISPATCHER_LIQUIDATOR::equals);

        if (isMotorized || isPicker || isDispatcher) {
            return user;
        }

        throw new OrderTrackerException(Constant.Error.NOT_PROFILE, Constant.ErrorCode.DEFAULT, HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void saveDeviceAndAssociateToCurrentMobileUser(DeviceDto deviceDto) {
        User user = findValidUser(SecurityUtils.getUID());
        Device device = new Device(deviceDto.getImei(), deviceDto.getPhoneNumber(), deviceDto.getPhoneMark(), deviceDto.getPhoneModel());
        device.setUser(user);
        device.setDeviceHistoryList(new ArrayList<>());
        device.getDeviceHistoryList().add(new DeviceHistory(user, DateUtil.currentDate()));
        deviceRepository.save(device);
    }

    @Override
    public User findValidUser(String uid) {
        User user = findUser(uid);
        if (!Status.Code.ENABLED.equals(user.getUserStatus().getCode())) {
            throw new OrderTrackerException("No estás habilitado en el Sistema", Constant.ErrorCode.DEFAULT, HttpStatus.FORBIDDEN);
        }
        return user;
    }

    @Override
    public User findUser(String uid) {
        return userRepository.getOne(uid);
    }
    
    @Override
    public void updateMotorizedStatus(String motorizedId, String status) {
    	updateMotorizedStatus(motorizedId, status, null);
    }

    @Override
    public void updateMotorizedStatus(String motorizedId, String status, String localCode) {
        log.info("[START] updateMotorizedStatus - motorizedId: {}", motorizedId);
    	User user = this.findUser(motorizedId);
    	Status.Code statusCode = Status.Code.parse(status);
    	Long parentId = null;    	
        
        if (Status.Code.NOT_AVAILABLE.equals(statusCode)) {
        	
        	parentId = userStatusHistoryRepository
        			.findLatestStatusHistory(user.getId(), Status.Code.AVAILABLE.name())
        			.map(p -> p.getId())
        			.orElseGet(() -> null);
        }    	
    	
    	createMotorizedStatusHistory(user, Status.Code.parse(status), localCode, parentId);
        log.info("status_name_user: {}", user.getTrackingStatus().getCode());
    	userRepository.save(user);
    }
    
    @Override
    public void updateUser(LoginDto loginDto, String motorizedId) {
        log.info("[START] updateUser - loginDto: {}, motorizedId: {}", loginDto, motorizedId);
    	if (LoginDto.Type.LOGOUT.equals(loginDto.getType())) {
    		User user = this.findUser(motorizedId); 
    		
    		userStatusHistoryRepository.findLatestStatusHistory(user.getId(), Status.Code.AVAILABLE.name())
    			.ifPresent(p -> {
    				
    				if (p.getChildStatusCode() == null) {
    					
    					createMotorizedStatusHistory(user, Status.Code.NOT_AVAILABLE, null, p.getId());
    				}
    				
    			});    		
    		
    		createMotorizedStatusHistory(user, Status.Code.OFFLINE, null, null);
    		
    		user.setType(null);
    		user.setCurrentLocal(null);
    		userRepository.save(user);
    	}
    }

    @Override
    public void updateMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto) {
        log.info("[START] updateMotorizedType - motorizedId: {}, updateMotorized - {}", motorizedId, motorizedTypeDto);
        User user = this.findUser(motorizedId);
        MotorizedType type = new MotorizedType();
        type.setCode(Constant.MotorizedType.parseByName(motorizedTypeDto.getType()));
        user.setType(type);
        user.setCurrentLocal(null);
        if (user.getTrackingStatus().getCode().equals(Status.Code.OFFLINE)) {
        	createMotorizedStatusHistory(user, Status.Code.ONLINE, null, null);
        }
        
        userRepository.save(user);
    }

    @Override
    public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, String localCode, String status) {
        log.info("[START] updateMotorizedType - motorizedId: {}, updateMotorized - {}, localCode - {}, status - {}", motorizedId, motorizedTypeDto, localCode, status);
        User user = this.findUser(motorizedId);
        MotorizedType type = new MotorizedType();
        type.setCode(Constant.MotorizedType.parseByName(motorizedTypeDto.getType()));
        user.setType(type);
        user.setCurrentLocal(localCode);

        Status.Code statusCode = Status.Code.parse(status);
        Long parentId = null;

        if (Status.Code.NOT_AVAILABLE.equals(statusCode)) {

            parentId = userStatusHistoryRepository
                    .findLatestStatusHistory(user.getId(), Status.Code.AVAILABLE.name())
                    .map(p -> p.getId())
                    .orElseGet(() -> null);
        }

        createMotorizedStatusHistory(user, statusCode, localCode, parentId);
        log.info("status_name_user: {}", user.getTrackingStatus().getCode());
        userRepository.save(user);
    }

    public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, Constant.MotorizedType currentMotorizedType, String localCode, String status) {}
    
    @Override
    public void newUser(UserDto userDto) {   
    	User user = CanonicalFactory.instanceOfTracker().userCanonicalFactory().toEntity(userDto);
    	user.setUserStatus(new UserStatus(Status.Code.ENABLED));
        userRepository.save(user);
    }
    
    @Override
    public void updateUser(UserDto userDto) {
    	User user = findById(userDto.getId());
    	if(user==null) {
    		throw new OrderTrackerException("Usuario no registrado", Constant.ErrorCode.USER_NO_REGISTERED, HttpStatus.NOT_FOUND);
    	}
    	//email no editable
    	userDto.setEmail(user.getEmail());
        CanonicalFactory.instanceOfTracker().userCanonicalFactory().toUpdated(userDto, user);
        userRepository.save(user);
    }
    
    @Override
    public void updatePhoneNumber(String userId, String phoneNumber) {
    	User user =  this.findUser(userId);
        user.setPhone(phoneNumber);
        userRepository.save(user);
    }
    
    @Override
    public Constant.MotorizedType getCurrentMotorizedType(String motorizedId) {
    	User user =  this.findUser(motorizedId);
    	if (user.getType() != null) {
    		return user.getType().getCode();
    	}
    	return null;
    }
    
    @Override
    public void updateMotorizedLocal(String motorizedId, String localCode, Constant.MotorizedType currentMotorizedType) {
    	User user =  this.findUser(motorizedId);
    	user.setCurrentLocal(localCode);
    	userRepository.save(user);
    }
    
    @Override
    public List<UserReportProjection> findUserByOrders(List<Long> orderIds) {
    	return userRepository.findMotorizedByOrders(orderIds);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
    
    private void createMotorizedStatusHistory(User user, Status.Code statusCode, String localCode, Long parentId) {
        
        if (!statusCode.equals(user.getTrackingStatus().getCode())) {  
        	
        	TrackingStatus trackingStatus = new TrackingStatus();
            trackingStatus.setCode(statusCode);
            user.setTrackingStatus(trackingStatus);

            UserStatusHistory motorizedStatusHistory = new UserStatusHistory();
            motorizedStatusHistory.setStatus(trackingStatus);
            motorizedStatusHistory.setTimeFromUi(DateUtil.currentDate());
            motorizedStatusHistory.setUserId(user);
            
            Optional.ofNullable(localCode).ifPresent(lc -> motorizedStatusHistory.setLocalCode(lc));
            Optional.ofNullable(parentId).ifPresent(pi -> motorizedStatusHistory.setParentId(pi));
            
            userStatusHistoryRepository.save(motorizedStatusHistory);
            
            //userRepository.save(user);
        }
    }    

    @Override
    public void updateDeviceVersion(String motorizedId, String version) {
        userRepository.updateDeviceVersion(motorizedId, version);
    }
    
    @Override
    public List<UserHistoryProjection> getUserStatusHistory(String userId, String status, LocalDate date, LocalDate incompleteHistorySearchDate) {
    	return userStatusHistoryRepository.findUserStatusHistory(userId, status, date, incompleteHistorySearchDate);
    }
}
