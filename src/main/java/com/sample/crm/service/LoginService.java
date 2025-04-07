package com.sample.crm.service;

import com.sample.crm.exception.UnauthorizedException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.model.security.LoginRequest;
import com.sample.crm.util.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service for managing user login and JWT generation.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    /**
     * Authenticates the user and generates a JWT token upon successful login.
     *
     * @param loginRequest The login request containing username and password.
     * @return A JWT token if authentication is successful.
     * @throws UnauthorizedException if authentication fails.
     */
    public String authenticateUser(LoginRequest loginRequest) {
        log.debug("Attempting to authenticate user: {}", loginRequest.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("User authenticated successfully: {}", loginRequest.getUsername());
        } catch (AuthenticationException authenticationException) {
            log.error("Authentication failed for user: {}", loginRequest.getUsername());
            throw new UnauthorizedException(ExceptionMessage.BAD_CREDENTIALS);
        }

        String jwtToken = jwtHelper.generateJwtToken(authentication);
        log.debug("Generated JWT token for user: {}", loginRequest.getUsername());
        return jwtToken;
    }
}
