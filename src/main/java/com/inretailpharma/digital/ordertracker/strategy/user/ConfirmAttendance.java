package com.inretailpharma.digital.ordertracker.strategy.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;
import com.inretailpharma.digital.ordertracker.dto.external.ConfirmAttendanceDto;
import com.inretailpharma.digital.ordertracker.service.external.AttendanceService;
import com.inretailpharma.digital.ordertracker.strategy.UserActionStrategy;
import com.inretailpharma.digital.ordertracker.transactions.UserTransaction;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ConfirmAttendance extends UpdateUser implements UserActionStrategy  {
	
	private AttendanceService attendanceService;

	@Autowired
	public ConfirmAttendance(UserTransaction userTransaction,
			AttendanceService attendanceService) {
		super(userTransaction);
		this.attendanceService = attendanceService;
	}
	
	@Override
	protected Mono<ResponseDTO<String>> validate(UpdateUserDto dto) {

		log.info("[START] validate");
		ConfirmAttendanceDto confirmAttendanceDto = ConfirmAttendanceDto.builder()
				.confirmationUser(dto.getUserId())
				.callBackId(dto.getCode())
				.build();
		
		return attendanceService.confirm(confirmAttendanceDto)
				.flatMap(r -> {
					if ("00".equals(r.getResult())) {
						return Mono.just(new ResponseDTO<String>(Constant.Response.SUCCESS));
					} else {
						return Mono.just(new ResponseDTO<String>(r.getResult(), r.getResultTitle(), r.getResultDetail()));
					}					
				});

	}
	
	@Override
	public void update(UpdateUserDto dto) {
		log.info("[START] update - ConfirmAttendance");
		this.userTransaction.updateMotorized(dto);
	}
}
