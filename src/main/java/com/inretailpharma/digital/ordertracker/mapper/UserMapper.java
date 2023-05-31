package com.inretailpharma.digital.ordertracker.mapper;

import java.util.Optional;

import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.dto.UserHistoryDto;
import com.inretailpharma.digital.ordertracker.entity.projection.UserHistoryProjection;
import com.inretailpharma.digital.ordertracker.entity.projection.UserReportProjection;
import com.inretailpharma.digital.ordertracker.utils.DateUtil;

public class UserMapper {
	
	private UserMapper() {
		
	}
	
	public static UserDto mapUserReportToDto(UserReportProjection user) {
		UserDto userDto = null;
		if (user != null) {
			userDto = new UserDto();
			userDto.setAlias(user.getAlias());
			userDto.setDni(user.getDni());
			userDto.setId(user.getId());
			userDto.setPhoneNumber(user.getPhone());
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setAssignedOrder(user.getAssignedOrder());
		}
		return userDto;
	}
	
	public static UserHistoryDto mapUserHistoryToDto(UserHistoryProjection history, String localName) {
		
		return Optional.ofNullable(history)
				.map(h -> 
					
					UserHistoryDto.builder()
					.userId(h.getUserId())
					.statusCode(h.getStatusCode())
					.startDate(DateUtil.getDateTimeFormatted(h.getCreationDate(), "hh:mma"))
					.endDate(DateUtil.getDateTimeFormatted(h.getChildCreationDate(), "hh:mma"))
					.localName(localName)
					.build()

				).orElseGet(() -> UserHistoryDto.builder().build());
	
	}

}
