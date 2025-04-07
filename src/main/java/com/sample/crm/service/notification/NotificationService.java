package com.sample.crm.service.notification;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.Notification;
import com.sample.crm.entity.User;
import com.sample.crm.entity.UserNotification;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.UserNotificationMapper;
import com.sample.crm.model.NotificationType;
import com.sample.crm.repository.NotificationRepository;
import com.sample.crm.repository.UserNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationMapper userNotificationMapper;

    public List<NotificationDTO> getAllNotificationsForUser(String username) {
        return userNotificationRepository.getAllByDismissedFalseAndUser_Username(username)
                .stream().map(userNotificationMapper::toDto).toList();
    }

    @Transactional
    public UserNotification createUserNotification(User receiver, NotificationType type, Map<String, String> params) {
        UserNotification userNotification = new UserNotification();
        Notification notification = createNotification(type);

        userNotification.setNotification(notification);
        userNotification.setParams(params);
        userNotification.setUser(receiver);
        userNotification.setDismissed(false);
        userNotification.setViewed(false);

        return userNotificationRepository.save(userNotification);
    }

    @Transactional
    public NotificationDTO updateNotification(NotificationDTO notificationDTO, Long id) {
        UserNotification userNotificationToUpdate = userNotificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND));

        userNotificationMapper.update(userNotificationToUpdate, notificationDTO);

        userNotificationRepository.save(userNotificationToUpdate);
        return notificationDTO;
    }

    @Transactional
    public NotificationDTO dismissNotification(long id) {
        UserNotification userNotification = userNotificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_FOUND));
        userNotification.setDismissed(true);
        userNotificationRepository.save(userNotification);
        return userNotificationMapper.toDto(userNotification);
    }

    private Notification createNotification(NotificationType type) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
}
