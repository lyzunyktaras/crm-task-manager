package com.sample.crm.service.notification;

import com.sample.crm.dto.TaskDTO;
import com.sample.crm.entity.Task;
import com.sample.crm.entity.User;
import com.sample.crm.model.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Component responsible for notifying users
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserNotifier {

    private final NotificationProcessor notificationProcessor;

    /**
     * Sends a notification to a user when the status of a task changes.
     *
     * @param user    The user to notify.
     * @param taskDTO The task with the updated status.
     */
    public void notifyOfTaskStatusChange(User user, TaskDTO taskDTO) {
        log.debug("Notifying user: {} about task status change. Task ID: {}, New Status: {}",
                user.getUsername(), taskDTO.getId(), taskDTO.getStatus());

        Map<String, String> params = new HashMap<>();
        params.put("task_description", taskDTO.getDescription());
        params.put("task_status", taskDTO.getStatus().name());

        notificationProcessor.buildAndSend(user, NotificationType.TASK_STATUS_CHANGED, params);

        log.debug("Notification sent to user: {} for task status change. Task ID: {}", user.getUsername(), taskDTO.getId());
    }

    /**
     * Sends a notification to a user when a task is approaching its due date.
     *
     * @param user The user to notify.
     * @param task The task nearing its due date.
     */
    public void notifyOfTaskDueDate(User user, Task task) {
        log.debug("Notifying user: {} about task due date. Task ID: {}, Description: {}",
                user.getUsername(), task.getId(), task.getDescription());

        Map<String, String> params = new HashMap<>();
        params.put("task_description", task.getDescription());

        notificationProcessor.buildAndSend(user, NotificationType.TASK_DUE_DATE, params);

        log.debug("Notification sent to user: {} for task due date. Task ID: {}", user.getUsername(), task.getId());
    }
}
