package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.UserDTO;
import com.sample.crm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = """
    Manages user operations including retrieval, creation, updating, subscribing users to clients, and deletion.
    It interacts with UserService to handle business logic such as encoding passwords, updating user information,
    and managing subscriptions between users and clients.
    """)
public class UserController {

  private final UserService userService;

  @Operation(
      summary = "Retrieve all users",
      description = """
          Fetches a complete list of all user records.
          Returns a list of UserDTO objects representing each user.
          """
  )
  @ApiResponse(responseCode = "200", description = "List of all users successfully retrieved.")
  @GetMapping
  public ResponseEntity<List<UserDTO>> findAllUsers() {
    return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
  }

  @Operation(
      summary = "Create a new user",
      description = """
          Creates a new user using the provided user data.
          The user's password is encoded before saving.
          Returns the created UserDTO object.
          """
  )
  @ApiResponse(responseCode = "201", description = "User successfully created.")
  @PostMapping
  public ResponseEntity<UserDTO> createUser(
      @Parameter(description = "User data for the new user, including username, password, and other details.", required = true)
      @RequestBody UserDTO userDTO) {
    return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update an existing user",
      description = """
          Updates an existing user record identified by the provided ID with new user data.
          Returns the updated UserDTO object.
          """
  )
  @ApiResponse(responseCode = "200", description = "User successfully updated.")
  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @Parameter(description = "Unique identifier of the user to be updated.", example = "4")
      @PathVariable Long id,
      @Parameter(description = "Updated user data.", required = true)
      @RequestBody UserDTO userDTO) {
    return new ResponseEntity<>(userService.updateUser(id, userDTO), HttpStatus.OK);
  }

  @Operation(
      summary = "Subscribe a user to a client",
      description = """
          Subscribes the user identified by the given user ID to the client identified by the client ID.
          If the user is already subscribed, no changes are made.
          Returns the updated UserDTO object.
          """
  )
  @ApiResponse(responseCode = "200", description = "User successfully subscribed to the client.")
  @PutMapping("/subscribe/{id}")
  public ResponseEntity<UserDTO> subscribeClient(
      @Parameter(description = "Unique identifier of the user to subscribe.", example = "4")
      @PathVariable("id") Long userId,
      @Parameter(description = "Unique identifier of the client to subscribe to.", example = "2")
      @RequestParam Long clientId) {
    return new ResponseEntity<>(userService.subscribeClient(userId, clientId), HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a user",
      description = """
          Deletes the user identified by the provided ID.
          Returns a 204 No Content status upon successful deletion.
          """
  )
  @ApiResponse(responseCode = "204", description = "User successfully deleted.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "Unique identifier of the user to delete.", example = "4")
      @PathVariable Long id) {
    userService.deleteUser(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
