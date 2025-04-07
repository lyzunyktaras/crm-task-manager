package com.sample.crm.service;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.dto.ContactDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.Contact;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.mapper.ContactMapper;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ContactService contactService;

    @Test
    void shouldFindAllContacts() {
        Contact contact = new Contact();
        contact.setId(1L);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);

        when(contactRepository.findAll()).thenReturn(List.of(contact));
        when(contactMapper.toDto(contact)).thenReturn(contactDTO);

        List<ContactDTO> result = contactService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void shouldSearchContacts() {
        Long clientId = 1L;
        String searchTerm = "Term";

        Contact contact = new Contact();
        contact.setId(1L);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);

        when(contactRepository.search(clientId, searchTerm)).thenReturn(List.of(contact));
        when(contactMapper.toDto(contact)).thenReturn(contactDTO);

        List<ContactDTO> result = contactService.search(clientId, searchTerm);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(contactRepository, times(1)).search(clientId, searchTerm);
    }

    @Test
    void shouldCreateContact() {
        Contact contact = new Contact();
        contact.setId(1L);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(1L);

        when(contactMapper.toEntity(contactDTO)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toDto(contact)).thenReturn(contactDTO);

        ContactDTO result = contactService.create(contactDTO);

        assertThat(result.getId()).isEqualTo(1L);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void shouldUpdateContact() {
        Long contactId = 1L;

        Contact contact = new Contact();
        contact.setId(contactId);

        Client client = new Client();
        client.setId(1L);

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactId);
        ClientDTO clientDTO= new ClientDTO();
        clientDTO.setId(1L);
        contactDTO.setClient(clientDTO);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toDto(contact)).thenReturn(contactDTO);

        ContactDTO result = contactService.update(contactId, contactDTO);

        assertThat(result.getId()).isEqualTo(contactId);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistentContact() {
        Long contactId = 1L;
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setId(contactId);

        when(contactRepository.findById(contactId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contactService.update(contactId, contactDTO))
                .isInstanceOf(NotFoundException.class);
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    void shouldDeleteContact() {
        Long contactId = 1L;

        contactService.delete(contactId);

        verify(contactRepository, times(1)).deleteById(contactId);
    }
}
