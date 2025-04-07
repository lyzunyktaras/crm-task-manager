package com.sample.crm.controller;


import com.sample.crm.exception.UnauthorizedException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.model.security.LoginRequest;
import com.sample.crm.service.LoginService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    @Test
    void shouldAuthenticateUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        String expectedJwt = "jwtToken";
        when(loginService.authenticateUser(loginRequest)).thenReturn(expectedJwt);

        ResponseEntity<String> response = loginController.authenticateUser(loginRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedJwt);
        verify(loginService, times(1)).authenticateUser(loginRequest);
    }
}
