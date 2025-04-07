package com.sample.crm.controller;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO client1;
    private ClientDTO client2;

    @BeforeEach
    public void setUp() {
        client1 = new ClientDTO();
        client1.setId(1L);
        client1.setCompanyName("Company Name");

        client2 = new ClientDTO();
        client2.setId(2L);
        client2.setCompanyName("Company Name");
    }

    @Test
    void shouldReturnAllClients() {
        List<ClientDTO> clients = List.of(client1, client2);
        when(clientService.findAll()).thenReturn(clients);

        ResponseEntity<List<ClientDTO>> response = clientController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(clients);
        verify(clientService, times(1)).findAll();
    }

    @Test
    void shouldReturnClientsForUser() {
        Long userId = 1L;
        List<ClientDTO> clients = List.of(client1);
        when(clientService.findAllForUser(userId)).thenReturn(clients);

        ResponseEntity<List<ClientDTO>> response = clientController.findForUser(userId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(clients);
        verify(clientService, times(1)).findAllForUser(userId);
    }

    @Test
    void shouldSearchClients() {
        String searchTerm = "Search";
        List<ClientDTO> clients = List.of(client1);
        when(clientService.search(searchTerm)).thenReturn(clients);

        ResponseEntity<List<ClientDTO>> response = clientController.search(searchTerm);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(clients);
        verify(clientService, times(1)).search(searchTerm);
    }

    @Test
    void shouldCreateClient() {
        ClientDTO clientDTO = client1;
        when(clientService.create(any(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.create(clientDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(clientDTO);
        verify(clientService, times(1)).create(clientDTO);
    }

    @Test
    void shouldUpdateClient() {
        Long clientId = 1L;
        ClientDTO clientDTO = client1;
        when(clientService.update(eq(clientId), any(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.update(clientId, clientDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(clientDTO);
        verify(clientService, times(1)).update(clientId, clientDTO);
    }

    @Test
    void shouldDeleteClient() {
        Long clientId = 1L;

        ResponseEntity<Void> response = clientController.delete(clientId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(clientService, times(1)).delete(clientId);
    }
}
