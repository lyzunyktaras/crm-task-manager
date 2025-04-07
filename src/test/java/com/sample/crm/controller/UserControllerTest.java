package com.sample.crm.controller;

import com.sample.crm.dto.UserDTO;
import com.sample.crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldReturnAllUsers() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");

        List<UserDTO> userDTOList = List.of(userDTO);

        when(userService.findAllUsers()).thenReturn(userDTOList);

        ResponseEntity<List<UserDTO>> response = userController.findAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDTOList);
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    void shouldCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");

        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setId(1L);
        createdUserDTO.setUsername("newUser");

        when(userService.createUser(userDTO)).thenReturn(createdUserDTO);

        ResponseEntity<UserDTO> response = userController.createUser(userDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdUserDTO);
        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    void shouldUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setUsername("updatedUser");

        when(userService.updateUser(1L, userDTO)).thenReturn(updatedUserDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(1L, userDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedUserDTO);
        verify(userService, times(1)).updateUser(1L, userDTO);
    }

    @Test
    void shouldSubscribeClientToUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testUser");

        when(userService.subscribeClient(1L, 2L)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.subscribeClient(1L, 2L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDTO);
        verify(userService, times(1)).subscribeClient(1L, 2L);
    }

    @Test
    void shouldDeleteUser() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userService, times(1)).deleteUser(1L);
    }
}
