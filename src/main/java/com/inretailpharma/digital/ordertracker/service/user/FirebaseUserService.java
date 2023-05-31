package com.inretailpharma.digital.ordertracker.service.user;

import static com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel.create;
import static com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil.FIREBASE_MOTORIZED_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.FIREBASE_DEVICE_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.FIREBASE_DRUGSTORE_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.FIREBASE_STATUS_PATH;
import static com.inretailpharma.digital.ordertracker.utils.Constant.Firebase.MOTORIZED_PATH;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;
import com.inretailpharma.digital.ordertracker.config.aditional.SecurityUtils;
import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedTypeDto;
import com.inretailpharma.digital.ordertracker.dto.ResetPasswordDto;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.entity.Device;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.exception.TrackerException;
import com.inretailpharma.digital.ordertracker.exception.UserException;
import com.inretailpharma.digital.ordertracker.firebase.core.FirebaseModel;
import com.inretailpharma.digital.ordertracker.firebase.model.DrugstoreCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedDeviceCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedStatusCanonical;
import com.inretailpharma.digital.ordertracker.firebase.model.MotorizedUserCanonical;
import com.inretailpharma.digital.ordertracker.firebase.service.InkaTrackerFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.service.InkaTrackerLiteFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.service.OrderTrackerFirebaseService;
import com.inretailpharma.digital.ordertracker.firebase.util.FirebaseUtil;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.service.parameter.ParameterService;
import com.inretailpharma.digital.ordertracker.service.system.DeviceService;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("firebaseUserService")
public class FirebaseUserService extends AbstractUserService implements UserService {

    @Autowired
    private OrderTrackerFirebaseService orderTrackerFirebaseService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InkaTrackerFirebaseService inkaTrackerFirebaseService;
    @Autowired
    private InkaTrackerLiteFirebaseService inkaTrackerLiteFirebaseService;
    @Autowired
    private TrackerUserService trackerUserService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private ExternalService externalService;
   
    
    @Override
    public void updateUser(LoginDto loginDto, String motorizedId) {
        log.info("[START] updateUser - loginDto: {}, motorizedId: {}", loginDto, motorizedId);
        User userMotorizado = userRepository.getOne(motorizedId);        
        
        if (LoginDto.Type.LOGIN.equals(loginDto.getType())) {
            log.info("updateUser - loginDto.getCurrentMotorizedType(): {}", loginDto.getCurrentMotorizedType());
        	
        	if (loginDto.getCurrentMotorizedType() == null) {

                log.info("updateUser - loginDto.getImei(): {}", loginDto.getImei());
                Device device = deviceService.findDeviceForImei(userMotorizado, loginDto.getImei());
//        		if (device != null) {
//                    log.info("updateUser - device.getImei(): {}", device.getImei());
                    orderTrackerFirebaseService.update(FirebaseUtil.createPath(MOTORIZED_PATH, motorizedId),
        	                FirebaseModel.create(FIREBASE_DEVICE_PATH, getDeviceCanonical(device, loginDto.getImei())));
//        		}
        		
        	}
        	
        } else if (LoginDto.Type.LOGOUT.equals(loginDto.getType())) {
        	
        	orderTrackerFirebaseService.delete(FIREBASE_MOTORIZED_PATH, motorizedId);
        	
        }
    }
	
	@Override
    public void updateMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto) {
        log.info("[START] updateMotorizedType - motorizedId: {}", motorizedId);
		User user = trackerUserService.findUser(motorizedId);
        log.info("status_name_user: {}", user.getTrackingStatus().getCode());

        MotorizedCanonical motorizado = new MotorizedCanonical();

        motorizado.setUser(userEntityToCanonical(user));
        
        Optional.ofNullable(motorizedTypeDto.getImei()).ifPresent(imei -> {
        	motorizado.setDevice(getDevice(user, imei));
        });        
        
        motorizado.setStatus(getStatus(user.getTrackingStatus().getCode()));
        
        if (motorizedTypeDto.getLatitude() != null && motorizedTypeDto.getLongitude() != null) {
            motorizado.setLatitude(motorizedTypeDto.getLatitude());
            motorizado.setLongitude(motorizedTypeDto.getLongitude());
        }

        log.info("motorizado.getStatus().getStatusName() (old): {}", motorizado.getStatus().getStatusName());
        if (Status.Code.ONLINE.name().equals(motorizado.getStatus().getStatusName())) {
        	motorizado.getStatus().setStatusName(Status.Code.AVAILABLE.name());
        }

        log.info("motorizado.getStatus().getStatusName() (new): {}", motorizado.getStatus().getStatusName());
        Constant.MotorizedType code = Constant.MotorizedType.parseByName(motorizedTypeDto.getType());
        log.info("code: {}", code);
        switch (code) {
	        case DELIVERY_CENTER:
	            inkaTrackerFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));
	            break;
	        case DRUGSTORE:
	        	inkaTrackerLiteFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));
	        	break;
	        default:
	        	throw new TrackerException(Constant.Error.INVALID_MOTORIZED_TYPE);
        }

        motorizado.setLocalType(code.name());        
        orderTrackerFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));  
    }

    @Override
    public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, String localCode, String status) {
    }

    public void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, Constant.MotorizedType currentMotorizedType, String localCode, String status) {
        log.info("[START] updateMotorized - motorizedId: {}, motorizedTypeDto: {}, currentMotorizedType: {}, " +
                "localCode: {}, status: {}", motorizedId, motorizedTypeDto, currentMotorizedType, localCode, status);
        User user = trackerUserService.findUser(motorizedId);
        log.info("status_name_user: {}", user.getTrackingStatus().getCode());

        MotorizedCanonical motorizado = new MotorizedCanonical();

        motorizado.setUser(userEntityToCanonical(user));

        Optional.ofNullable(motorizedTypeDto.getImei()).ifPresent(imei -> {
            motorizado.setDevice(getDevice(user, imei));
        });

        motorizado.setStatus(getStatus(Status.Code.parse(status)));

        if (motorizedTypeDto.getLatitude() != null && motorizedTypeDto.getLongitude() != null) {
            motorizado.setLatitude(motorizedTypeDto.getLatitude());
            motorizado.setLongitude(motorizedTypeDto.getLongitude());
        }

        log.info("motorizado.getStatus().getStatusName() (old): {}", motorizado.getStatus().getStatusName());
        if (Status.Code.ONLINE.name().equals(motorizado.getStatus().getStatusName())) {
            motorizado.getStatus().setStatusName(Status.Code.AVAILABLE.name());
        }

        log.info("motorizado.getStatus().getStatusName() (new): {}", motorizado.getStatus().getStatusName());
        Constant.MotorizedType code = Constant.MotorizedType.parseByName(motorizedTypeDto.getType());
        log.info("code: {}", code);
        switch (code) {
            case DELIVERY_CENTER:
                inkaTrackerFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));
                break;
            case DRUGSTORE:
                inkaTrackerLiteFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));
                break;
            default:
                throw new TrackerException(Constant.Error.INVALID_MOTORIZED_TYPE);
        }

        motorizado.setLocalType(code.name());
        orderTrackerFirebaseService.update(MOTORIZED_PATH, create(motorizedId, motorizado));

        this.updateMotorizedLocal(motorizedId, localCode, currentMotorizedType);
    }

    @Override
    public void saveDeviceAndAssociateToCurrentMobileUser(DeviceDto deviceDto) {

        trackerUserService.saveDeviceAndAssociateToCurrentMobileUser(deviceDto);

        User user = trackerUserService.findValidMobileUser(SecurityUtils.getUID());

        Constant.Application application = Constant.Application.parseByRole(user.getRoleList().get(BigDecimal.ZERO.intValue()).getCode());

        orderTrackerFirebaseService.update(FirebaseUtil.createPath(application.path(), user.getId()),
                FirebaseModel.create(FIREBASE_DEVICE_PATH, getDevice(user, deviceDto.getImei())));
    }

    @Override
    public void updateMotorizedStatus(String motorizedId, String status) {

        log.info("[START] updateMotorizedStatus - motorizedId: {}, status: {}", motorizedId, status);
        MotorizedStatusCanonical motorizedDto = new MotorizedStatusCanonical();
        motorizedDto.setStatusDate(DateUtil.currentDateLong());
        motorizedDto.setStatusName(status);
        log.info("motorizedDto.getStatusName(): {}", motorizedDto.getStatusName());
         orderTrackerFirebaseService.update(FIREBASE_MOTORIZED_PATH + "/" + motorizedId,
                FirebaseModel.create(FIREBASE_STATUS_PATH, motorizedDto));
    }

    private MotorizedDeviceCanonical getDevice(User user, String imei) {
        log.info("Search  ime: {} userExternalId:  {}", imei, user.getId());
        Device device = deviceService.findDeviceForImei(user, imei);
        return getDeviceCanonical(device, imei);
    }
    
    private MotorizedDeviceCanonical getDeviceCanonical(Device device, String imei) {
        MotorizedDeviceCanonical canonical = new MotorizedDeviceCanonical();
        canonical.setImei(imei);
        if (device != null) {
            canonical.setPhoneNumber(device.getPhoneNumber());
            canonical.setPhoneMark(device.getPhoneMark());
            canonical.setPhoneModel(device.getPhoneModel());        	
        }
        return canonical;
    }
    
    private MotorizedUserCanonical userEntityToCanonical(User user) {
        MotorizedUserCanonical canonical = new MotorizedUserCanonical();
        canonical.setAlias(user.getAlias());
        canonical.setFirstName(user.getFirstName());
        canonical.setLastName(user.getLastName());
        canonical.setPhone(user.getPhone());
        canonical.setPhoto(user.getUrlPhoto());
        canonical.setDni(user.getDni());
        return canonical;
    }
    
    private MotorizedStatusCanonical getStatus(Status.Code code) {
        MotorizedStatusCanonical canonical = new MotorizedStatusCanonical();
        canonical.setStatusName(code.name());
        canonical.setStatusDate(DateUtil.currentDate().getTime());
        return canonical;
    }
    
    @Override
    public void newUser(UserDto userDto) {   
    	String password = new String(Base64.getDecoder().decode(userDto.getPassword()));
        
    	CreateRequest createRequest = new CreateRequest()
    	    .setEmail(userDto.getEmail())
    	    .setEmailVerified(false)
    	    .setPassword(password)
    	    .setDisplayName(userDto.getAlias())
    	    .setDisabled(false);

        orderTrackerFirebaseService.newUser(createRequest);
    }
    
    @Override
    public void updateUser(UserDto userDto) {   
    	UserRecord user = orderTrackerFirebaseService.findUserByEmail(userDto.getEmail());
    	log.info("Updating user in firebase, user: {}", user.toString());
        UpdateRequest updateRequest = new UpdateRequest(user.getUid())
    	    .setDisplayName(userDto.getAlias());
        
        if(!StringUtils.isEmpty(userDto.getPassword())) updateRequest.setPassword(new String(Base64.getDecoder().decode(userDto.getPassword())));
        if(!StringUtils.isEmpty(userDto.getUserStatus()) && userDto.getUserStatus().equals(Status.Code.ENABLED.name())) updateRequest.setDisabled(false);
        if(!StringUtils.isEmpty(userDto.getUserStatus()) && userDto.getUserStatus().equals(Status.Code.DISABLED.name())) updateRequest.setDisabled(true);

        orderTrackerFirebaseService.updateUser(updateRequest);
    }

	@Override
	public void updateUserPassword(ResetPasswordDto resetPasswordDto) {
		
		UserRecord user = orderTrackerFirebaseService.findUserByEmail(resetPasswordDto.getEmail());
		if (user == null) {
			log.error("User with email: " + resetPasswordDto.getEmail() + " does not exist in OrderTracker");
			throw new UserException("User with email: " + resetPasswordDto.getEmail() + " does not exist in OrderTracker");
		}
		
		UpdateRequest updateRequest = new UpdateRequest(user.getUid())
				.setPassword(resetPasswordDto.getPassword());
		
		orderTrackerFirebaseService.updateUser(updateRequest);
	}
	
	@Override
    public void updateMotorizedLocal(String motorizedId, String localCode, Constant.MotorizedType currentMotorizedType) {
				
		Optional.ofNullable(externalService.findDrugstore(localCode)).ifPresent(local -> {

			DrugstoreCanonical canonical = new DrugstoreCanonical();
	        canonical.setId(local.getLegacyId());
	        canonical.setLatitude(local.getLatitude().doubleValue());
	        canonical.setLongitude(local.getLongitude().doubleValue());
	        canonical.setLocalCode(local.getLocalCode());
	        canonical.setLocalName(local.getName());
	        canonical.setAddress(local.getAddress());
	        Integer drugstoreRadius = parameterService.parameterByCode(Constant.DRUGSTORE_RADIUS).getIntValue();
	        canonical.setRadius(drugstoreRadius);

	        orderTrackerFirebaseService.update(FirebaseUtil.createPath(MOTORIZED_PATH, motorizedId),
	                FirebaseModel.create(FIREBASE_DRUGSTORE_PATH, canonical));
	        
	        switch (currentMotorizedType) {
	        case DELIVERY_CENTER:
	        	inkaTrackerFirebaseService.update(FirebaseUtil.createPath(MOTORIZED_PATH, motorizedId),
		                FirebaseModel.create(FIREBASE_DRUGSTORE_PATH, canonical));
	            break;
	        case DRUGSTORE:
	        	inkaTrackerLiteFirebaseService.update(FirebaseUtil.createPath(MOTORIZED_PATH, motorizedId),
		                FirebaseModel.create(FIREBASE_DRUGSTORE_PATH, canonical));
	        	break;
	        default:
	        	throw new TrackerException(Constant.Error.INVALID_MOTORIZED_TYPE);
	        }
		});
    }
}
