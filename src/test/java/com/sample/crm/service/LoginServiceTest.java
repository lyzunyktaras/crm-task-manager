package com.sample.crm.service;

import com.sample.crm.exception.UnauthorizedException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.model.security.LoginRequest;
import com.sample.crm.util.JwtHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtHelper jwtHelper;

    @InjectMocks
    private LoginService loginService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("testUser", "testPassword");
        Authentication authentication = mock(Authentication.class);
        String expectedJwt = "jwtToken";

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()))).thenReturn(authentication);
        when(jwtHelper.generateJwtToken(authentication)).thenReturn(expectedJwt);

        String jwtToken = loginService.authenticateUser(loginRequest);

        assertEquals(expectedJwt, jwtToken);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtHelper, times(1)).generateJwtToken(authentication);
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenAuthenticationFails() {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())))
                .thenThrow(new UnauthorizedException(ExceptionMessage.BAD_CREDENTIALS));

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> loginService.authenticateUser(loginRequest)
        );

        assertEquals(ExceptionMessage.BAD_CREDENTIALS.name(), exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtHelper, never()).generateJwtToken(any(Authentication.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
