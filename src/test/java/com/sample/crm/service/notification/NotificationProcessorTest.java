package com.sample.crm.service.notification;


import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.User;
import com.sample.crm.entity.UserNotification;
import com.sample.crm.mapper.UserNotificationMapper;
import com.sample.crm.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationProcessorTest {

    @Mock
    private UserNotificationMapper userNotificationMapper;

    @Mock
    private NotificationService notificationService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private NotificationProcessor notificationProcessor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationProcessor, "queueGlobal", "/queue/notification");
    }

    @Test
    void shouldBuildAndSendNotification() {
        User user = new User();
        user.setUsername("testUser");

        NotificationDTO notificationDTO = new NotificationDTO();
        Map<String, String> params = Map.of("key", "value");

        when(notificationService.createUserNotification(user, NotificationType.TASK_STATUS_CHANGED, params))
                .thenReturn(new UserNotification());
        when(userNotificationMapper.toDto(any(UserNotification.class)))
                .thenReturn(notificationDTO);

        notificationProcessor.buildAndSend(user, NotificationType.TASK_STATUS_CHANGED, params);

        verify(notificationService, times(1))
                .createUserNotification(user, NotificationType.TASK_STATUS_CHANGED, params);
        verify(userNotificationMapper, times(1)).toDto(any(UserNotification.class));
        verify(simpMessagingTemplate, times(1))
                .convertAndSendToUser(eq(user.getUsername()), eq("/queue/notification"), eq(notificationDTO));
    }
}
