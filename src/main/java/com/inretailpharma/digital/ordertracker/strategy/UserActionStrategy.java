package com.inretailpharma.digital.ordertracker.strategy;

import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;

import reactor.core.publisher.Mono;

public interface UserActionStrategy {	

	Mono<ResponseDTO<String>> process(UpdateUserDto dto);
}
