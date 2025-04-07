package com.sample.crm.controller;


import com.sample.crm.dto.ContactDTO;
import com.sample.crm.service.ContactService;
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
class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @Test
    void shouldFindAllContacts() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);
        contactDTO.setFirstName("first");
        contactDTO.setLastName("last");

        List<ContactDTO> contacts = List.of(contactDTO);
        when(contactService.findAll()).thenReturn(contacts);

        ResponseEntity<List<ContactDTO>> response = contactController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(contacts);
        verify(contactService, times(1)).findAll();
    }

    @Test
    void shouldSearchContacts() {
        Long clientId = 1L;
        String searchTerm = "first";
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);
        contactDTO.setFirstName("first");
        contactDTO.setLastName("last");

        List<ContactDTO> contacts = List.of(contactDTO);
        when(contactService.search(clientId, searchTerm)).thenReturn(contacts);

        ResponseEntity<List<ContactDTO>> response = contactController.search(clientId, searchTerm);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(contacts);
        verify(contactService, times(1)).search(clientId, searchTerm);
    }

    @Test
    void shouldCreateContact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("first");
        contactDTO.setLastName("last");

        ContactDTO createdContactDTO = new ContactDTO();
        createdContactDTO.setId(1L);
        createdContactDTO.setFirstName("first");
        createdContactDTO.setLastName("last");

        when(contactService.create(contactDTO)).thenReturn(createdContactDTO);

        ResponseEntity<ContactDTO> response = contactController.create(contactDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(createdContactDTO);
        verify(contactService, times(1)).create(contactDTO);
    }

    @Test
    void shouldUpdateContact() {
        Long id = 1L;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setFirstName("first");
        contactDTO.setLastName("last");

        ContactDTO updatedContactDTO = new ContactDTO();
        updatedContactDTO.setId(id);
        updatedContactDTO.setFirstName("first");
        updatedContactDTO.setLastName("last");

        when(contactService.update(id, contactDTO)).thenReturn(updatedContactDTO);

        ResponseEntity<ContactDTO> response = contactController.update(id, contactDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedContactDTO);
        verify(contactService, times(1)).update(id, contactDTO);
    }

    @Test
    void shouldDeleteContact() {
        Long id = 1L;

        doNothing().when(contactService).delete(id);

        ResponseEntity<Void> response = contactController.delete(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(contactService, times(1)).delete(id);
    }
}
