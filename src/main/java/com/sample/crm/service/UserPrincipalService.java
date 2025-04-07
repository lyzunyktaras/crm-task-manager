package com.sample.crm.service;

import com.sample.crm.entity.User;
import com.sample.crm.model.security.UserPrincipal;
import com.sample.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserPrincipalService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by username and converts it to UserPrincipal.
     *
     * @param username The username of the user to load.
     * @return UserDetails representing the authenticated user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User Not Found with username: " + username);
                });

        log.debug("User loaded successfully: {}", username);
        return UserPrincipal.fromUser(user);
    }
}
