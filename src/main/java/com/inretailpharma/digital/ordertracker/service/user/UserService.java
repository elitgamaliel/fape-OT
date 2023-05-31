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

public interface UserService {

    void updateUser(LoginDto loginDto, String motorizedId);

    List<UserStatusHistory> findLastStatusHistoryMap(User user);

    UserStatusHistory findLastStatusHistory(String userId, TrackingStatus status);

    User findByEmail(String email);

    User findValidUser(String uid);

    void saveDeviceAndAssociateToCurrentMobileUser(DeviceDto deviceDto);

    User findValidMobileUser(String userId);

    User findUser(String uid);
    
    void updateMotorizedStatus(String motorizedId, String status);

    void updateMotorizedStatus(String motorizedId, String status, String localCode);

    void updateMotorizedStatusLocation(String motorizedId, String status, MotorizedTypeDto motorizedTypeDto);

    void updateMotorizedType(String motorizedId, MotorizedTypeDto motorizedTypeDto);
    void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, String localCode, String status);
    void updateMotorized(String motorizedId, MotorizedTypeDto motorizedTypeDto, Constant.MotorizedType currentMotorizedType, String localCode, String status);

    void newUser(UserDto userDto);
    
    void updateUser(UserDto userDto);
    
    User findById(String id);
    
    void updateUserPassword(ResetPasswordDto resetPasswordDto);
    
    void updatePhoneNumber(String userId, String phoneNumber);
    
    Constant.MotorizedType getCurrentMotorizedType(String motorizedId);
    
    void updateMotorizedLocal(String motorizedId, String localCode, Constant.MotorizedType currentMotorizedType);
    
    List<UserReportProjection> findUserByOrders(List<Long> orderIds);

    void updateDeviceVersion(String motorizedId, String version);
    
    List<UserHistoryProjection> getUserStatusHistory(String userId, String status, LocalDate date, LocalDate incompleteHistorySearchDate);
}
