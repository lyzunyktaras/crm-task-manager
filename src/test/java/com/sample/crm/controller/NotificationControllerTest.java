package com.sample.crm.controller;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.model.NotificationType;
import com.sample.crm.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private NotificationController notificationController;

  @Test
  void shouldGetAllNotificationsForUser() {
    String username = "testUser";
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setId(1L);
    notificationDTO.setType(NotificationType.TASK_STATUS_CHANGED);
    when(notificationService.getAllNotificationsForUser(username)).thenReturn(List.of(notificationDTO));

    ResponseEntity<List<NotificationDTO>> response = notificationController.getAllNotificationsForUser(username);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull().hasSize(1);
    assertThat(response.getBody().get(0).getType()).isEqualTo(NotificationType.TASK_STATUS_CHANGED);
    verify(notificationService, times(1)).getAllNotificationsForUser(username);
  }

  @Test
  void shouldUpdateNotification() {
    long notificationId = 1L;
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setId(notificationId);
    notificationDTO.setType(NotificationType.TASK_STATUS_CHANGED);
    when(notificationService.updateNotification(notificationDTO, notificationId)).thenReturn(notificationDTO);

    ResponseEntity<NotificationDTO> response = notificationController.updateNotification(notificationDTO, notificationId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getType()).isEqualTo(NotificationType.TASK_STATUS_CHANGED);
    verify(notificationService, times(1)).updateNotification(notificationDTO, notificationId);
  }

  @Test
  void shouldDismissNotification() {
    long notificationId = 1L;
    NotificationDTO notificationDTO = new NotificationDTO();
    notificationDTO.setId(notificationId);
    notificationDTO.setType(NotificationType.TASK_DUE_DATE);
    when(notificationService.dismissNotification(notificationId)).thenReturn(notificationDTO);

    ResponseEntity<NotificationDTO> response = notificationController.updateNotification(notificationDTO, notificationId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getType()).isEqualTo(NotificationType.TASK_DUE_DATE);
    verify(notificationService, times(1)).dismissNotification(notificationId);
  }
}
