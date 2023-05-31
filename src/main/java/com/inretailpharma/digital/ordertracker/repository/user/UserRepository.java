package com.inretailpharma.digital.ordertracker.repository.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inretailpharma.digital.ordertracker.entity.MotorizedType;
import com.inretailpharma.digital.ordertracker.entity.TrackingStatus;
import com.inretailpharma.digital.ordertracker.entity.User;
import com.inretailpharma.digital.ordertracker.entity.UserStatusHistory;
import com.inretailpharma.digital.ordertracker.entity.projection.UserReportProjection;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select new com.inretailpharma.digital.ordertracker.entity.UserStatusHistory(h.status, max(h.timeFromUi)) from User u left join u.statusHistoryList as h where u.id = :userId group by h.status order by h.timeFromUi desc")
    List<UserStatusHistory> findLastStatusHistoryMap(@Param("userId") String userId);

    @Query("select new com.inretailpharma.digital.ordertracker.entity.UserStatusHistory(h.status, h.timeFromUi, h.latitude, h.longitude) from User u left join u.statusHistoryList as h where u.id = :userId and h.status = :status order by h.timeFromUi desc")
    Stream<UserStatusHistory> findLastStatusHistory(@Param("userId") String userId, @Param("status") TrackingStatus status);

    User findByEmail(String email);

    Optional<User> findById(String uid);
    
    @Query(value="select t from User u inner join u.type as t where u.id = :userId ")
    MotorizedType findMotorizedType(@Param("userId") String userId);
    
    @Query(value="select m.id as id, m.dni as dni, m.firstName as firstName, m.lastName as lastName, m.alias as alias, m.phone as phone, o.ecommercePurchaseId as assignedOrder from OrderTracker o inner join o.motorized as m where o.ecommercePurchaseId in (:orderIds)")
    List<UserReportProjection> findMotorizedByOrders(@Param("orderIds") List<Long> orderIds);

    @Modifying
    @Transactional
    @Query(value = " UPDATE user" +
            " SET current_version = :currentVersion" +
            " WHERE  id = :motorizedId",
            nativeQuery = true)
    void updateDeviceVersion(@Param("motorizedId") String motorizedId
                             , @Param("currentVersion") String currentVersion);
}