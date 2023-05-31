package com.inretailpharma.digital.ordertracker.strategy.user;

import com.inretailpharma.digital.ordertracker.dto.ResponseDTO;
import com.inretailpharma.digital.ordertracker.dto.UpdateUserDto;
import com.inretailpharma.digital.ordertracker.strategy.UserActionStrategy;
import reactor.core.publisher.Mono;

public class OnRoute implements UserActionStrategy {

    @Override
    public Mono<ResponseDTO<String>> process(UpdateUserDto dto) {
        return null;
    }
}
