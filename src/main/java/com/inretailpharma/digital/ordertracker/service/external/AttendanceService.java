package com.inretailpharma.digital.ordertracker.service.external;

import com.inretailpharma.digital.ordertracker.dto.external.ConfirmAttendanceDto;
import com.inretailpharma.digital.ordertracker.dto.external.ConfirmAttendanceRespDto;

import reactor.core.publisher.Mono;

public interface AttendanceService {
	
	Mono<ConfirmAttendanceRespDto> confirm(ConfirmAttendanceDto confirmAttendanceDto);
}
