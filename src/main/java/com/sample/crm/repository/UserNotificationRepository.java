package com.sample.crm.repository;

import com.sample.crm.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> getAllByDismissedFalseAndUser_Username(String username);
}

