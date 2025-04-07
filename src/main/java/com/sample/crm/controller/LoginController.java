package com.sample.crm.controller;

import com.sample.crm.model.security.LoginRequest;
import com.sample.crm.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
@Tag(name = "Authentication Controller", description = """
    Handles user authentication operations.
    It supports user login and generates a JWT token upon successful authentication.
    In case of invalid credentials, an UnauthorizedException is thrown.""")
public class LoginController {

  private final LoginService loginService;

  @Operation(
      summary = "Authenticate user",
      description = """
          Authenticates the user using the provided credentials.
          Accepts a LoginRequest object containing the username and password.
          Returns a JWT token if authentication is successful.
          Throws an UnauthorizedException if the authentication fails."""
  )
  @ApiResponse(responseCode = "200", description = "User authenticated successfully and JWT token is returned.")
  @PostMapping("/login")
  public ResponseEntity<String> authenticateUser(
      @Parameter(description = "Login credentials containing username and password", required = true)
      @Valid @RequestBody LoginRequest loginRequest) {
    return new ResponseEntity<>(loginService.authenticateUser(loginRequest), HttpStatus.OK);
  }
}
