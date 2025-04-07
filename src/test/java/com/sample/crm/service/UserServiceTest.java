package com.sample.crm.service;

import com.sample.crm.dto.UserDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.UserMapper;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldFindAllUsers() {
        User user = new User();
        UserDTO userDTO = new UserDTO();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.findAllUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");

        User user = new User();
        user.setUsername("testUser");

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(passwordEncoder.encode(null)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertEquals(userDTO.getUsername(), result.getUsername());
        verify(userMapper, times(1)).toEntity(userDTO);
        verify(passwordEncoder, times(1)).encode(null);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void shouldUpdateUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");

        User user = new User();
        user.setId(userId);
        user.setUsername("originalUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(null)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userId, userDTO);

        assertEquals(userDTO.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).update(user, userDTO);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserToUpdateNotFound() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, userDTO));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldSubscribeClientToUser() {
        Long userId = 1L;
        Long clientId = 2L;
        User user = new User();
        Client client = new Client();

        user.setClients(new ArrayList<>());
        client.setUsers(new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(userMapper.toDto(null)).thenReturn(new UserDTO());

        UserDTO result = userService.subscribeClient(userId, clientId);

        assertNotNull(result);
        assertTrue(user.getClients().contains(client));
        verify(userRepository, times(1)).findById(userId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(client);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldNotSubscribeClientIfAlreadySubscribed() {
        Long userId = 1L;
        Long clientId = 2L;
        User user = new User();
        Client client = new Client();
        client.setId(clientId);

        user.setClients(List.of(client));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(userMapper.toDto(user)).thenReturn(new UserDTO());

        UserDTO result = userService.subscribeClient(userId, clientId);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInSubscribeClient() {
        Long userId = 1L;
        Long clientId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.subscribeClient(userId, clientId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundInSubscribeClient() {
        Long userId = 1L;
        Long clientId = 2L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.subscribeClient(userId, clientId));
        verify(userRepository, times(1)).findById(userId);
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void shouldDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
