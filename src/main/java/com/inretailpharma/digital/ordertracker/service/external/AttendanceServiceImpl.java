package com.inretailpharma.digital.ordertracker.service.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.inretailpharma.digital.ordertracker.config.OrderTrackerProperties;
import com.inretailpharma.digital.ordertracker.dto.external.ConfirmAttendanceDto;
import com.inretailpharma.digital.ordertracker.dto.external.ConfirmAttendanceRespDto;
import com.inretailpharma.digital.ordertracker.utils.Constant;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	private OrderTrackerProperties orderTrackerProperties;
	
	@Autowired
	public AttendanceServiceImpl(OrderTrackerProperties orderTrackerProperties) {
		this.orderTrackerProperties = orderTrackerProperties;
	}
	
	@Override
	public Mono<ConfirmAttendanceRespDto> confirm(ConfirmAttendanceDto confirmAttendanceDto) {
		
		log.info("[START] AttendanceService.confirm - uri:{} - body:{}",
    			orderTrackerProperties.getConfirmAttendanceUrl(), confirmAttendanceDto);

		return WebClient
				.builder()
				.baseUrl(orderTrackerProperties.getConfirmAttendanceUrl())
				.build()
				.patch()
				.bodyValue(confirmAttendanceDto)
				.retrieve()
				.bodyToMono(ConfirmAttendanceRespDto.class)
				.doOnSuccess(r -> log.info("[END] AttendanceService.confirm - response {}", r))
				.defaultIfEmpty( new ConfirmAttendanceRespDto(Constant.Response.ERROR, "EMPTY", "EMPTY"))
				.onErrorResume(ex -> {
					log.error("[ERROR] AttendanceService.confirm - error:{}, body:{}", ex.getMessage()
							, confirmAttendanceDto);
					ex.printStackTrace();
					return Mono.just(new ConfirmAttendanceRespDto(Constant.Response.ERROR, "", ex.getMessage()));
				});
	}
}
