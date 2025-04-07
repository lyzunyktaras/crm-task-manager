package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.NotificationDTO;
import com.sample.crm.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = """
    Manages notification operations for users.
    It supports retrieving notifications for a specific user, updating a notification, and dismissing notifications.
    The controller interacts with NotificationService to handle business logic such as updating notifications and marking them as dismissed.
    """)
public class NotificationController {

  private final NotificationService notificationService;

  @Operation(
      summary = "Retrieve notifications for a user",
      description = """
          Retrieves all notifications that have not been dismissed for the specified user.
          The username is provided as a path variable.
          Returns a list of NotificationDTO objects representing the notifications.
          """
  )
  @ApiResponse(responseCode = "200", description = "List of notifications successfully retrieved.")
  @GetMapping("/{username}")
  public ResponseEntity<List<NotificationDTO>> getAllNotificationsForUser(
      @Parameter(description = "Username of the user for whom notifications are to be retrieved.", example = "username")
      @PathVariable String username) {
    return new ResponseEntity<>(notificationService.getAllNotificationsForUser(username), HttpStatus.OK);
  }

  @Operation(
      summary = "Update a notification",
      description = """
          Updates an existing notification record with new details provided in the request body.
          The notification to update is identified by its unique ID in the path variable.
          Returns the updated NotificationDTO object.
          """
  )
  @ApiResponse(responseCode = "200", description = "Notification successfully updated.")
  @PutMapping("/{id}")
  public ResponseEntity<NotificationDTO> updateNotification(
      @Parameter(description = "Notification data with updated fields.", required = true)
      @Valid @RequestBody NotificationDTO notificationDTO,
      @Parameter(description = "Unique identifier of the notification to update.", example = "10")
      @PathVariable long id) {
    return new ResponseEntity<>(notificationService.updateNotification(notificationDTO, id), HttpStatus.OK);
  }

  @Operation(
      summary = "Dismiss a notification",
      description = """
          Marks a notification as dismissed.
          The notification is identified by its unique ID provided in the path variable.
          Returns the updated NotificationDTO object with the dismissed status.
          """
  )
  @ApiResponse(responseCode = "200", description = "Notification successfully dismissed.")
  @PatchMapping("/{id}")
  public ResponseEntity<NotificationDTO> dismissNotification(
      @Parameter(description = "Unique identifier of the notification to dismiss.", example = "10")
      @PathVariable long id) {
    return new ResponseEntity<>(notificationService.dismissNotification(id), HttpStatus.OK);
  }
}
