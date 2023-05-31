package com.inretailpharma.digital.ordertracker.service.user;

import java.time.LocalDate;
import java.util.List;

import com.inretailpharma.digital.ordertracker.dto.DeviceDto;
import com.inretailpharma.digital.ordertracker.dto.LoginDto;
import com.inretailpharma.digital.ordertracker.dto.MotorizedTypeDto;
import com.inretailpharma.digital.ordertracker.dto.ResetPasswordDto;
import com.inretailpharma.digital.ordertracker.dto.UserDto;
import com.inretailpharma.digital.ordertracker.entity.TrackingStatus;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.UserStatusHistory;
import com.inretailpharma.digital.ordertracker.entity.projection.UserHistoryProjection;
import com.inretailpharma.digital.ordertracker.entity.projection.UserReportProjection;
import com.inretailpharma.digital.ordertracker.utils.Constant;

public abstract class AbstractUserService implements UserService {

    @Override
    public void updateUser(LoginDto loginDto, String motorizedId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserStatusHistory> findLastStatusHistoryMap(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserStatusHistory findLastStatusHistory(String userId, TrackingStatus status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User findByEmail(String email) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User findValidMobileUser(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User findValidUser(String uid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveDeviceAndAssociateToCurrentMobileUser(DeviceDto deviceDto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User findUser(String uid) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void updateMotorizedStatus(String motorizedId, String status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateMotorizedStatus(String motorizedId, String status, String localCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateMotorizedStatusLocation(String motorizedId, String status, MotorizedTypeDto motorizedTypeDto) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void newUser(UserDto userDto) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void updateUser(UserDto userDto) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public User findById(String id) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void updateUserPassword(ResetPasswordDto resetPasswordDto) {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public void updatePhoneNumber(String userId, String phoneNumber) {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public Constant.MotorizedType getCurrentMotorizedType(String motorizedId) {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public void updateMotorizedLocal(String motorizedId, String localCode, Constant.MotorizedType currentMotorizedType) {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public List<UserReportProjection> findUserByOrders(List<Long> orderIds) {
    	throw new UnsupportedOperationException();
    }

    @Override
    public void updateDeviceVersion(String motorizedId, String version) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<UserHistoryProjection> getUserStatusHistory(String userId, String status, LocalDate date, LocalDate incompleteHistorySearchDate) {
        throw new UnsupportedOperationException();
    }
}
