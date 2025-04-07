package com.sample.crm.mapper;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserNotificationDecorator implements UserNotificationMapper {
    @Autowired
    @Qualifier("delegate")
    private UserNotificationMapper userNotificationMapper;

    @Override
    public NotificationDTO toDto(UserNotification userNotification) {
        NotificationDTO notificationDTO = userNotificationMapper.toDto(userNotification);

        notificationDTO.setType(userNotification.getNotification().getType());
        notificationDTO.setSentAt(
                userNotification.getNotification().getSentAt().toString());

        return notificationDTO;
    }
}
