package com.itson.profeco.repository;

import com.itson.profeco.model.UserNotification;
import com.itson.profeco.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, UUID> {


    Page<UserNotification> findByUserOrderByCreatedAtDesc(UserEntity user, Pageable pageable);


    List<UserNotification> findByUserAndIsReadFalseOrderByCreatedAtDesc(UserEntity user);


    long countByUserAndIsReadFalse(UserEntity user);


    @Modifying
    @Query("UPDATE UserNotification un SET un.isRead = true WHERE un.user = :user AND un.isRead = false")
    int markAllAsReadForUser(@Param("user") UserEntity user);
}