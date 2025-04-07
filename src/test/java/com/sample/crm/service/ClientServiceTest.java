package com.sample.crm.service;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.dto.UserDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.User;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.ClientMapper;
import com.sample.crm.mapper.UserMapper;
import com.sample.crm.model.security.UserPrincipal;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.UserRepository;
import com.sample.crm.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldFindAllClients() {
        Client client = new Client();
        client.setId(1L);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);

        when(clientRepository.findAll()).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllClientsForUser() {
        Long userId = 1L;
        Client client = new Client();
        client.setId(1L);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);

        when(clientRepository.findAllByUserId(userId)).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.findAllForUser(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(clientRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void shouldSearchClients() {
        String searchTerm = "Test Client";

        Client client = new Client();
        client.setId(1L);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);

        when(clientRepository.search(searchTerm)).thenReturn(List.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);

        List<ClientDTO> result = clientService.search(searchTerm);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(clientRepository, times(1)).search(searchTerm);
    }

    @Test
    void shouldCreateClient() {
        Client client = new Client();
        client.setId(1L);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);

        User user = new User();
        user.setId(1L);
        user.setUsername("username");

        UserPrincipal userPrincipal = new UserPrincipal(1L, "username", "password");

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(userPrincipal);

            when(clientMapper.toEntity(clientDTO)).thenReturn(client);
            when(userRepository.findByUsername(userPrincipal.getUsername())).thenReturn(Optional.of(user));
            when(clientRepository.save(client)).thenReturn(client);
            when(clientMapper.toDto(client)).thenReturn(clientDTO);

            ClientDTO result = clientService.create(clientDTO);

            assertThat(result.getId()).isEqualTo(1L);
            verify(clientRepository, times(1)).save(client);
        }
    }

    @Test
    void shouldUpdateClient() {
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        clientDTO.setUsers(List.of(userDTO));

        User user = new User();
        user.setId(1L);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(clientDTO);
        when(clientRepository.save(client)).thenReturn(client);

        ClientDTO result = clientService.update(clientId, clientDTO);

        assertThat(result.getId()).isEqualTo(clientId);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentClient() {
        Long clientId = 1L;
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.update(clientId, clientDTO))
                .isInstanceOf(NotFoundException.class);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void shouldDeleteClient() {
        Long clientId = 1L;

        clientService.delete(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }
}
