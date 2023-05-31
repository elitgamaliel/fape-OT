package com.inretailpharma.digital.ordertracker.repository.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inretailpharma.digital.ordertracker.entity.UserStatusHistory;
import com.inretailpharma.digital.ordertracker.entity.projection.UserHistoryProjection;

public interface UserStatusHistoryRepository extends JpaRepository<UserStatusHistory, Integer>{
	
	
	@Query(value = "(SELECT h.id, h.user_id userId, h.status_code statusCode, h.time_from_ui creationDate, h.local_code localCode, c.status_code childStatusCode, c.time_from_ui childCreationDate"
			+ " FROM user_status_history h LEFT JOIN user_status_history c ON (h.id = c.parent_id)"
			+ " WHERE h.user_id = (:user_id) AND h.status_code = (:status) AND h.time_from_ui > (:incompleteHistorySearchDate) AND c.status_code IS NULL ORDER BY h.time_from_ui desc) UNION DISTINCT"
			+ "(SELECT h.id, h.user_id userId, h.status_code statusCode, h.time_from_ui creationDate, h.local_code localCode, c.status_code childStatusCode, c.time_from_ui childCreationDate"
			+ " FROM user_status_history h LEFT JOIN user_status_history c ON (h.id = c.parent_id)"
			+ " WHERE h.user_id = (:user_id) AND h.status_code = (:status) AND h.time_from_ui > (:date) ORDER BY h.time_from_ui desc)", nativeQuery = true)
    List<UserHistoryProjection> findUserStatusHistory(@Param("user_id") String user_id, @Param("status") String status, @Param("date") LocalDate date, @Param("incompleteHistorySearchDate") LocalDate incompleteHistorySearchDate);
	
	@Query(value = "SELECT h.id, h.user_id userId, h.status_code statusCode, h.time_from_ui creationDate, h.local_code localCode, c.status_code childStatusCode, c.time_from_ui childCreationDate"
			+ " FROM user_status_history h LEFT JOIN user_status_history c ON (h.id = c.parent_id)"
			+ " WHERE h.user_id = (:user_id) AND h.status_code = (:status) ORDER BY h.time_from_ui desc LIMIT 1", nativeQuery = true)
	Optional<UserHistoryProjection> findLatestStatusHistory(@Param("user_id") String user_id, @Param("status") String status);
}
