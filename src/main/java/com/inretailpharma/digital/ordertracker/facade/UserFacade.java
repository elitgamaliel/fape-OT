package com.inretailpharma.digital.ordertracker.facade;

import com.inretailpharma.digital.ordertracker.config.SmsProperties;
import com.inretailpharma.digital.ordertracker.dto.*;
import com.inretailpharma.digital.ordertracker.dto.in.SendSmsIn;
import com.inretailpharma.digital.ordertracker.entity.DeliveryTravel;
import com.inretailpharma.digital.ordertracker.entity.Status;
import com.inretailpharma.digital.ordertracker.exception.UserException;
import com.inretailpharma.digital.ordertracker.mapper.UserMapper;
import com.inretailpharma.digital.ordertracker.service.external.ExternalService;
import com.inretailpharma.digital.ordertracker.service.external.ExternalSmsService;
import com.inretailpharma.digital.ordertracker.service.parameter.ParameterService;
import com.inretailpharma.digital.ordertracker.service.system.DeliveryTravelService;
import com.inretailpharma.digital.ordertracker.service.user.UserService;
import com.inretailpharma.digital.ordertracker.strategy.UserActionStrategy;
import com.inretailpharma.digital.ordertracker.transactions.UserTransaction;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import com.inretailpharma.digital.ordertracker.utils.Constant.UserAction;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;
import com.inretailpharma.digital.ordertracker.utils.HashGenerator;
import com.inretailpharma.digital.ordertracker.utils.RandomCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserFacade {

    private UserTransaction userTransaction;
    private DeliveryTravelService deliveryTravelService;    
    private ExternalSmsService externalSmsService;    
    private SmsProperties smsProperties;     
    private UserService trackerUserService;
    private Map<Constant.UserAction, UserActionStrategy> userActionsCache;
    private ExternalService externalService;
    private ParameterService parameterService;

    @Autowired
    public UserFacade(UserTransaction userTransaction,
    		DeliveryTravelService deliveryTravelService, 
    		ExternalSmsService externalSmsService,
    		SmsProperties smsProperties,
    		List<UserActionStrategy> userActions,
    		@Qualifier("trackerUserService") UserService trackerUserService,
    		ExternalService externalService,
			ParameterService parameterService) {
    	
    	this.userTransaction = userTransaction;
    	this.deliveryTravelService = deliveryTravelService;
    	this.externalSmsService = externalSmsService;
    	this.smsProperties = smsProperties;
    	this.trackerUserService = trackerUserService;
    	this.externalService = externalService;
    	this.parameterService = parameterService;

    	userActionsCache = Arrays
                .stream(Constant.UserAction.values())
                .collect(Collectors.toMap(p -> p, p -> 

                	userActions.stream()
                		.filter(f -> f.getClass().equals(p.getImplementationClass()))
                		.findAny().get()
                ));

    }
    
    public ResponseDTO<String> register(LoginDto loginDto, String motorizedId) {
		log.info("[START] register - loginDto: {}, motorizedId: {}", loginDto, motorizedId);
        loginDto.setType(LoginDto.Type.LOGIN);        
        userTransaction.updatePhoneNumber(motorizedId, loginDto.getPhoneNumber());
        return userTransaction.updateMotorized(loginDto, motorizedId);        
    }

    public ResponseDTO<String> unRegister(String motorizedId) {
        LoginDto loginDto = new LoginDto();
        loginDto.setType(LoginDto.Type.LOGOUT);
        checkIfMotorizedhasPendingTravels(motorizedId);
        return userTransaction.updateMotorized(loginDto, motorizedId);
    }

    public void changeStatus(String motorizedId, String status) {
    	userTransaction.updateStatusMotorized(motorizedId, status, null);
    }
    
    public String generateCodeSms(SendSmsIn sendSmsIn) {
        String code = RandomCodeGenerator.nextCode();
        sendSmsIn.setContent(MessageFormat.format(smsProperties.getMessageCodeFarmamoto(), code));
        
        externalSmsService.sendSms(sendSmsIn);
        
        return HashGenerator.hash(code);
    }
    
    public void changeMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto) {    	
    	checkIfMotorizedhasPendingTravels(motorizedId);
    	userTransaction.updateMotorizedType(motorizedId, motorizedTypeDto);
    }
    
    public void create(UserDto userDto) {
        userTransaction.create(userDto);        
    }
    
    public void update(UserDto userDto) {
    	userTransaction.update(userDto);        
    }
    
    public void resertPassword(ResetPasswordDto resetPasswordDto) {
    	if (StringUtils.isEmpty(resetPasswordDto.getPassword())) {
    		throw new UserException("Empty password");
    	}
    	userTransaction.resertPassword(resetPasswordDto);
    }
    
    public void changeCurrentLocal(String motorizedId, String localCode) {    	
    	userTransaction.changeCurrentLocal(motorizedId, localCode);
    }
    
    public List<UserDto> getMotorizedByOrders(List<Long> orderIds) {    	
    	return trackerUserService.findUserByOrders(orderIds)
    			.stream()
    			.map(UserMapper::mapUserReportToDto)
    			.collect(Collectors.toList());
    }

    public void updateDeviceVersion(String motorizedId, String version) {
        userTransaction.updateDeviceVersion(motorizedId, version);
    }
    
    public Mono<ResponseDTO<UpdateUserResponseDto>> processUserAction(UpdateUserDto dto) {

		log.info("[START] processUserAction");
    	Constant.UserAction action = UserAction.getByName(dto.getAction());
    	
    	
    	if (action == null || Constant.UserAction.NONE.equals(action))
    	{
    		return Mono.just(new ResponseDTO<UpdateUserResponseDto>(Constant.Response.ERROR, "Invalid Action", "Invalid Action"));
    		
    	} else { 
    		
    		Optional<DrugstoreDto> local = Optional.ofNullable(
    				Optional.ofNullable(dto.getLocalCode())    				 
    				.map(lc -> Optional.ofNullable(externalService.findDrugstore(lc)).orElse(null))
    				.orElse(null)
    		);
    		
    		local.ifPresent(l -> dto.setLocalType(Constant.MotorizedType.parseByNameNV(l.getLocalType())));    		
    		
    		dto.setStatusName(action.getStatusName());
    		return userActionsCache.get(action).process(dto)
    		.flatMap(result -> {    			
    			
    			UpdateUserResponseDto data = new UpdateUserResponseDto();
    			data.setCreationDate(DateUtil.currentDateLong());
    			
    			local.ifPresent(l -> data.setLocal(
    					DrugstoreHeaderDto.builder()
						.latitude(l.getLatitude())
						.longitude(l.getLongitude())
						.localCode(l.getLocalCode())
						.localType(l.getLocalType())
						.name(l.getName())
						.build()
				));    			
    			
    			return Mono.just(new ResponseDTO<UpdateUserResponseDto>(result.getCode(), result.getTitle(), result.getMessage(), data));
    			
    		}).onErrorResume(ex -> {
				
				log.error("[ERROR] processUserAction - error:{}, body {}"
						, ex.getMessage(), dto);
				ex.printStackTrace();

				return Mono.just(new ResponseDTO<UpdateUserResponseDto>(Constant.Response.ERROR, "ERROR!", ex.getMessage()));
			});
    		

    	}
    }

    public ResponseDTO<List<UserHistoryDto>> getAvailabilityHistory(String userId) {
    	
    	ResponseDTO<List<UserHistoryDto>> response = new ResponseDTO<>(Constant.CODE_SUCCESS);
    	List<DrugstoreDto> locals = externalService.findAllDrugstores();

		Long searchDays = Long.valueOf(parameterService.parameterByCode(Constant.INCOMPLETE_AVAILABILITY_IN_DAYS)
				.getValue());
		List<UserHistoryDto> data = trackerUserService.getUserStatusHistory(
    			userId,
    			Status.Code.AVAILABLE.name(),
    			LocalDate.now(),
				LocalDate.now().minusDays(searchDays))
    		.stream()
    		.map(h -> {
    			
    			String localName = locals.stream()
						.filter(f -> f.getLocalCode().equals(h.getLocalCode())).findAny()
						.map(DrugstoreDto::getName)
						.orElse(null);
    			
    			return UserMapper.mapUserHistoryToDto(h, localName);
    		})
    		.collect(Collectors.toList());    	
    	
    	response.setData(data);

    	return response;
    }
    
    private void checkIfMotorizedhasPendingTravels(String motorizedId) {
        List<DeliveryTravel> travelsNotFinalized = deliveryTravelService.findDeliveryTravelNotFinalized(motorizedId);
        travelsNotFinalized.forEach(travel -> log.info("#User {} cannot logout because travel {} is not finalized.", motorizedId, travel.getGroupName()));
        if (!travelsNotFinalized.isEmpty()) {
         	throw new UserException(Constant.Error.USER_CANNOT_LOGOUT, Constant.ErrorCode.USER_CANNOT_LOGOUT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
