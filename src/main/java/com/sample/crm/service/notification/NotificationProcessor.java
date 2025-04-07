package com.sample.crm.service.notification;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.User;
import com.sample.crm.mapper.UserNotificationMapper;
import com.sample.crm.model.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Processor for building and sending notifications to users via WebSocket.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationProcessor {

    @Value("${spring.websocket.queue.notification}")
    private String queueGlobal;

    private final UserNotificationMapper userNotificationMapper;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * Asynchronously builds and sends a notification to a user.
     *
     * @param receiver The user who will receive the notification.
     * @param type     The type of notification to send.
     * @param params   Additional parameters for the notification.
     */
    @Async
    public void buildAndSend(User receiver, NotificationType type, Map<String, String> params) {
        log.debug("Building notification for user: {}, type: {}", receiver.getUsername(), type);

        Map<User, NotificationDTO> notificationMap = new HashMap<>();
        NotificationDTO notificationDTO = userNotificationMapper.toDto(
                notificationService.createUserNotification(receiver, type, params)
        );
        notificationMap.put(receiver, notificationDTO);

        log.debug("Notification built for user: {}. Sending notification...", receiver.getUsername());
        send(notificationMap);
    }

    /**
     * Sends notifications to users via WebSocket.
     *
     * @param notificationMap A map of users to their respective notifications.
     */
    private void send(Map<User, NotificationDTO> notificationMap) {
        notificationMap.forEach((user, notification) -> {
            simpMessagingTemplate.convertAndSendToUser(user.getUsername(), queueGlobal, notification);
            log.debug("Notification sent to user: {}", user.getUsername());
        });
    }
}
