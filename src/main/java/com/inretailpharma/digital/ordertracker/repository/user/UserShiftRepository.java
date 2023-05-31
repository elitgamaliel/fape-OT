package com.inretailpharma.digital.ordertracker.repository.user;

import com.inretailpharma.digital.ordertracker.entity.UserShift;
import com.inretailpharma.digital.ordertracker.entity.custom.UserShiftId;
import com.inretailpharma.digital.ordertracker.utils.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserShiftRepository extends JpaRepository<UserShift, UserShiftId> {
    @Modifying
    @Query(value = "update UserShift set isActive = :active where id.user.id = :motorizedId")
    void updateUserShiftStatusByMotorizedId(@Param("active") Constant.Logical active, @Param("motorizedId") String motorizedId);

    UserShift findFirstById_User_IdAndId_Shift_Id(String userId, Integer shiftId);

    UserShift findFirstById_User_Id(String userId);
}
