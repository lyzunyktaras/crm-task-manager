package com.sample.crm.mapper;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.UserNotification;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
@DecoratedWith(UserNotificationDecorator.class)
public interface UserNotificationMapper extends EntityMapper<NotificationDTO, UserNotification> {
    NotificationDTO toDto(UserNotification userNotification);

    UserNotification toEntity(NotificationDTO notificationDTO);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "notification", ignore = true)
    void update(@MappingTarget UserNotification userNotification, NotificationDTO notificationDTO);
}
