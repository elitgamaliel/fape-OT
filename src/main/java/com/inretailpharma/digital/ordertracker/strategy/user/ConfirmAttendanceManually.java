package com.inretailpharma.digital.ordertracker.strategy.user;

import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;
import com.inretailpharma.digital.ordertracker.strategy.UserActionStrategy;
import com.inretailpharma.digital.ordertracker.transactions.UserTransaction;

@Component
public class ConfirmAttendanceManually extends UpdateUser implements UserActionStrategy  {

	public ConfirmAttendanceManually(UserTransaction userTransaction) {
		super(userTransaction);
	}
	
	@Override
	public void update(UpdateUserDto dto) {
		this.userTransaction.updateMotorized(dto);
	}
}
