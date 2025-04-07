package com.sample.crm.service.notification;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Task;
import com.sample.crm.entity.User;
import com.sample.crm.model.NotificationType;
import com.sample.crm.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserNotifierTest {

    @Mock
    private NotificationProcessor notificationProcessor;

    @InjectMocks
    private UserNotifier userNotifier;

    @Test
    void shouldNotifyOfTaskStatusChange() {
        User user = new User();
        user.setUsername("testUser");

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setDescription("Test Task");
        taskDTO.setStatus(TaskStatus.IN_PROGRESS);

        userNotifier.notifyOfTaskStatusChange(user, taskDTO);

        Map<String, String> expectedParams = Map.of(
                "task_description", taskDTO.getDescription(),
                "task_status", taskDTO.getStatus().name()
        );
        verify(notificationProcessor, times(1))
                .buildAndSend(user, NotificationType.TASK_STATUS_CHANGED, expectedParams);
    }

    @Test
    void shouldNotifyOfTaskDueDate() {
        User user = new User();
        user.setUsername("testUser");

        Task task = new Task();
        task.setDescription("Due Task");

        userNotifier.notifyOfTaskDueDate(user, task);

        Map<String, String> expectedParams = Map.of("task_description", task.getDescription());
        verify(notificationProcessor, times(1))
                .buildAndSend(user, NotificationType.TASK_DUE_DATE, expectedParams);
    }
}
