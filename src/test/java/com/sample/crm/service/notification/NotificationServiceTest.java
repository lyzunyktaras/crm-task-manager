package com.sample.crm.service.notification;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.entity.Notification;
import com.sample.crm.entity.User;
import com.sample.crm.entity.UserNotification;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.UserNotificationMapper;
import com.sample.crm.model.NotificationType;
import com.sample.crm.repository.NotificationRepository;
import com.sample.crm.repository.UserNotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private UserNotificationMapper userNotificationMapper;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldGetAllNotificationsForUser() {
        String username = "testUser";
        UserNotification userNotification = new UserNotification();
        NotificationDTO notificationDTO = new NotificationDTO();

        when(userNotificationRepository.getAllByDismissedFalseAndUser_Username(username))
                .thenReturn(List.of(userNotification));
        when(userNotificationMapper.toDto(userNotification)).thenReturn(notificationDTO);

        List<NotificationDTO> result = notificationService.getAllNotificationsForUser(username);

        assertEquals(1, result.size());
        verify(userNotificationRepository, times(1))
                .getAllByDismissedFalseAndUser_Username(username);
        verify(userNotificationMapper, times(1)).toDto(userNotification);
    }

    @Test
    void shouldCreateUserNotification() {
        User user = new User();
        user.setUsername("testUser");

        NotificationType type = NotificationType.TASK_STATUS_CHANGED;
        Map<String, String> params = Map.of("key", "value");

        Notification notification = new Notification();
        UserNotification userNotification = new UserNotification();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(userNotificationRepository.save(any(UserNotification.class))).thenReturn(userNotification);

        UserNotification result = notificationService.createUserNotification(user, type, params);

        assertNotNull(result);
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(userNotificationRepository, times(1)).save(any(UserNotification.class));
    }

    @Test
    void shouldUpdateNotification() {
        Long id = 1L;
        NotificationDTO notificationDTO = new NotificationDTO();
        UserNotification userNotification = new UserNotification();

        when(userNotificationRepository.findById(id)).thenReturn(Optional.of(userNotification));

        NotificationDTO result = notificationService.updateNotification(notificationDTO, id);

        assertEquals(notificationDTO, result);
        verify(userNotificationRepository, times(1)).findById(id);
        verify(userNotificationMapper, times(1)).update(userNotification, notificationDTO);
        verify(userNotificationRepository, times(1)).save(userNotification);
    }

    @Test
    void shouldThrowExceptionWhenNotificationToUpdateNotFound() {
        Long id = 1L;
        NotificationDTO notificationDTO = new NotificationDTO();

        when(userNotificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationService.updateNotification(notificationDTO, id));
        verify(userNotificationRepository, times(1)).findById(id);
    }

    @Test
    void shouldDismissNotification() {
        Long id = 1L;
        UserNotification userNotification = new UserNotification();

        when(userNotificationRepository.findById(id)).thenReturn(Optional.of(userNotification));
        when(userNotificationMapper.toDto(userNotification)).thenReturn(new NotificationDTO());

        NotificationDTO result = notificationService.dismissNotification(id);

        assertNotNull(result);
        assertTrue(userNotification.isDismissed());
        verify(userNotificationRepository, times(1)).findById(id);
        verify(userNotificationRepository, times(1)).save(userNotification);
    }

    @Test
    void shouldThrowExceptionWhenNotificationToDismissNotFound() {
        Long id = 1L;

        when(userNotificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationService.dismissNotification(id));
        verify(userNotificationRepository, times(1)).findById(id);
    }
}
