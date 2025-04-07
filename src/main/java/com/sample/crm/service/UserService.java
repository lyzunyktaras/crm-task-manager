package com.sample.crm.service;

import com.sample.crm.dto.UserDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.UserMapper;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Retrieves all users.
     *
     * @return List of all users as UserDTO.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        log.debug("Fetching all users from the database");
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }

    /**
     * Creates a new user with encoded password.
     *
     * @param userDTO The user data to create.
     * @return The created user as UserDTO.
     */
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.debug("Creating a new user: {}", userDTO.getUsername());
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Updates an existing user.
     *
     * @param id      The ID of the user to update.
     * @param userDTO The updated user data.
     * @return The updated user as UserDTO.
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.debug("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", id);
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                });
        userMapper.update(user, userDTO);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Subscribes a user to a client.
     *
     * @param userId   The ID of the user to subscribe.
     * @param clientId The ID of the client to subscribe to.
     * @return The updated user as UserDTO.
     */
    @Transactional
    @CacheEvict(value = "clients", allEntries = true)
    public UserDTO subscribeClient(Long userId, Long clientId) {
        log.debug("Subscribing user ID: {} to client ID: {}", userId, clientId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                });

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.error("Client not found with ID: {}", clientId);
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                });

        if (user.getClients().stream().map(Client::getId).toList().contains(client.getId())) {
            log.debug("User ID: {} is already subscribed to client ID: {}", userId, clientId);
            return userMapper.toDto(user);
        }

        if (user.getClients().isEmpty()) {
            user.setClients(new ArrayList<>());
        }

        user.getClients().add(client);
        client.getUsers().add(user);

        clientRepository.save(client);
        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     */
    public void deleteUser(Long id) {
        log.debug("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }
}
