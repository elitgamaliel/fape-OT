package com.inretailpharma.digital.ordertracker.dto.factory;

import java.util.Optional;
import java.util.stream.Collectors;

import com.inretailpharma.digital.ordertracker.dto.TrackerReasonDto;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.entity.Role;
import com.inretailpharma.digital.ordertracker.entity.TrackerReason;
import com.inretailpharma.digital.ordertracker.entity.TrackingStatus;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.UserStatus;
import com.inretailpharma.digital.ordertracker.entity.Status;



public class TrackerCanonicalFactory {
	
	private final UserCanonicalFactory userCanonicalFactory = new UserCanonicalFactory();
	
	private static final CanonicalFactory.ParseCanonicalFactory<TrackerReason, TrackerReasonDto> reasonCanonicalFactory = reason -> {
        TrackerReasonDto canonical = new TrackerReasonDto();
        canonical.setId(reason.getCode());
        canonical.setReason(reason.getReason());
        return canonical;
    };
    
    public CanonicalFactory.ParseCanonicalFactory<TrackerReason, TrackerReasonDto> reasonCanonicalFactory() {
        return reasonCanonicalFactory;
    }
    
    public UserCanonicalFactory userCanonicalFactory() {
        return userCanonicalFactory;
    }
    
    public class UserCanonicalFactory implements CanonicalFactory.CreatedCanonicalFactory<User, UserDto>, CanonicalFactory.UpdatedCanonicalFactory<User, UserDto> {

        @Override
        public User toEntity(UserDto dto) {
            User user = new User();
            user.setTrackingStatus(new TrackingStatus(Status.Code.OFFLINE));
            toUpdated(dto, user);
            return user;
        }

        @Override
        public void toUpdated(UserDto dto, User entity) {            
            entity.setId(dto.getId());
            Optional.ofNullable(dto.getUserStatus()).ifPresent(status ->
                entity.setUserStatus(new UserStatus(Status.Code.parse(status)))
            );
            entity.setEmployeeCode(dto.getEmployeeCode());
            entity.setDni(dto.getDni());
            entity.setEmail(dto.getEmail());
            entity.setFirstName(dto.getFirstName());
            entity.setLastName(dto.getLastName());
            entity.setAlias(dto.getAlias());
            entity.setDrugstoreId(dto.getDrugstoreId());
            entity.setRoleList(dto.getRoles().stream().map(code -> new Role(Role.Code.valueOf(code))).collect(Collectors.toList()));
        }
    }
}
