package com.inretailpharma.digital.ordertracker.strategy.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;
import com.inretailpharma.digital.ordertracker.strategy.UserActionStrategy;
import com.inretailpharma.digital.ordertracker.transactions.UserTransaction;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UpdateUser implements UserActionStrategy {
	
	protected UserTransaction userTransaction;
	
	@Autowired
	public UpdateUser(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}	
	
	protected Mono<ResponseDTO<String>> validate(UpdateUserDto dto) {
		return Mono.just(new ResponseDTO<String>(Constant.Response.SUCCESS));
	}	
	
	protected void update(UpdateUserDto dto) {		
		userTransaction.updateStatusMotorized(dto.getUserId(), dto.getStatusName(), dto.getLocalCode());
	}	
	
	@Override
	public Mono<ResponseDTO<String>> process(UpdateUserDto dto) {

		log.info("[START] UpdateUserStrategy.updateUser - body {}"
				, dto);
		
		return this.validate(dto)
				.flatMap( v -> {
					log.info("[START] process - flatMap() {}" , v);
					return Mono.just(Optional.ofNullable(v)
						.map(result -> {
							
							if (Constant.Response.SUCCESS.equals(result.getCode())) {								
									this.update(dto);
							}
		
							return result;
							
						}).orElseGet(() -> {							
							return new ResponseDTO<String>(Constant.Response.ERROR, "EMPTY", "EMPTY");
						}));
					
				});
	}

}