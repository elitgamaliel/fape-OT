package com.inretailpharma.digital.ordertracker.service.motorized;

import com.inretailpharma.digital.ordertracker.entity.Shift;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.UserShift;
import com.inretailpharma.digital.ordertracker.entity.custom.UserShiftId;
import com.inretailpharma.digital.ordertracker.exception.OrderTrackerException;
import com.inretailpharma.digital.ordertracker.repository.ShiftRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserRepository;
import com.inretailpharma.digital.ordertracker.repository.user.UserShiftRepository;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final UserShiftRepository userShiftRepository;
    private final UserRepository userRepository;

    public ShiftService(final ShiftRepository shiftRepository,
                        final UserShiftRepository userShiftRepository,
                        final UserRepository userRepository) {
        this.shiftRepository = shiftRepository;
        this.userShiftRepository = userShiftRepository;
        this.userRepository = userRepository;
    }

    public List<Shift> findAllActiveShifts() {
        return shiftRepository.findAllByIsEnabled(Constant.Logical.Y);
    }

    public void saveShift(Shift shift) {
        shiftRepository.save(shift);
    }

    public void updateShift(Shift shift) {
        Shift dbShift = shiftRepository.findById(shift.getId()).get();
        if (dbShift != null) {
            dbShift.setAmountMotorized(shift.getAmountMotorized());
            dbShift.setCode(shift.getCode());
            dbShift.setEndHour(shift.getEndHour());
            dbShift.setStartHour(shift.getStartHour());
            dbShift.setStartBreakHour(shift.getStartBreakHour());
            dbShift.setEndBreakHour(shift.getEndBreakHour());
            shiftRepository.save(dbShift);
        } else {
            throw new OrderTrackerException("Id not found", Constant.ErrorCode.DEFAULT, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteShift(Integer shiftId) {
        Shift shift = shiftRepository.findById(shiftId).get();
        if (shift != null) {
            shift.setIsEnabled(Constant.Logical.N);
            shiftRepository.save(shift);
        } else {
            throw new OrderTrackerException("Id not found", Constant.ErrorCode.DEFAULT, HttpStatus.NOT_FOUND);
        }
    }

    public void associate(String userId, Integer shiftId) {
        if (shiftId != null) {
            userShiftRepository.updateUserShiftStatusByMotorizedId(Constant.Logical.N, userId);
            UserShift userShift = userShiftRepository.findFirstById_User_IdAndId_Shift_Id(userId, shiftId);
            if (userShift == null) {
                userShift = new UserShift();
                User motorized = userRepository.findById(userId).orElse(null);
                Shift shift = shiftRepository.findById(shiftId).orElse(null);
                userShift.setId(new UserShiftId(motorized, shift));
                userShift.setIsActive(Constant.Logical.Y);
                userShiftRepository.save(userShift);
            } else {
                userShift.setIsActive(Constant.Logical.Y);
                userShiftRepository.save(userShift);
            }
        }
    }

    public Shift findShiftById(Integer shiftId) {
        return shiftRepository.findById(shiftId).get();
    }

    public UserShift getUserShif(String userId) {
        return userShiftRepository.findFirstById_User_Id(userId);
    }
}
