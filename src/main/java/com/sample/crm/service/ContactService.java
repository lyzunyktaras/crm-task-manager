package com.sample.crm.service;

import com.sample.crm.dto.ContactDTO;
import com.sample.crm.entity.Contact;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.ContactMapper;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final ClientRepository clientRepository;

    /**
     * Retrieves all contacts, with caching enabled.
     *
     * @return List of all contacts as ContactDTO.
     */
    @Cacheable(value = "contacts")
    @Transactional(readOnly = true)
    public List<ContactDTO> findAll() {
        log.debug("Fetching all contacts from the database");
        return contactRepository.findAll().stream().map(contactMapper::toDto).toList();
    }

    /**
     * Searches contacts by client ID and search term.
     *
     * @param clientId   The ID of the client to search within.
     * @param searchTerm The search term for filtering contacts.
     * @return List of matching contacts as ContactDTO.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> search(Long clientId, String searchTerm) {
        log.debug("Searching contacts for client ID: {} with term: {}", clientId, searchTerm);
        return contactRepository.search(clientId, searchTerm).stream().map(contactMapper::toDto).toList();
    }

    /**
     * Creates a new contact and clears the cache.
     *
     * @param contactDTO The contact data to create.
     * @return The created contact as ContactDTO.
     */
    @Transactional
    @CacheEvict(value = "contacts", allEntries = true)
    public ContactDTO create(ContactDTO contactDTO) {
        log.debug("Creating a new contact: {}", contactDTO);
        return contactMapper.toDto(contactRepository.save(contactMapper.toEntity(contactDTO)));
    }

    /**
     * Updates an existing contact and clears the cache.
     *
     * @param id         The ID of the contact to update.
     * @param contactDTO The updated contact data.
     * @return The updated contact as ContactDTO.
     */
    @Transactional
    @CacheEvict(value = "contacts", allEntries = true)
    public ContactDTO update(Long id, ContactDTO contactDTO) {
        log.debug("Updating contact with ID: {}", id);
        Contact contact = getContact(id);
        contactMapper.update(contact, contactDTO);
        contact.setClient(clientRepository.findById(contactDTO.getClient().getId())
                .orElseThrow(() -> {
                    log.error("Client not found with ID: {}", contactDTO.getClient().getId());
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                }));
        return contactMapper.toDto(contactRepository.save(contact));
    }

    /**
     * Deletes a contact by ID and clears the cache.
     *
     * @param id The ID of the contact to delete.
     */
    @CacheEvict(value = "contacts", allEntries = true)
    public void delete(Long id) {
        log.debug("Deleting contact with ID: {}", id);
        contactRepository.deleteById(id);
    }

    /**
     * Retrieves a contact entity by ID.
     *
     * @param id The ID of the contact to retrieve.
     * @return The contact entity.
     * @throws NotFoundException if the contact is not found.
     */
    private Contact getContact(Long id) {
        log.debug("Fetching contact with ID: {}", id);
        return contactRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found with ID: {}", id);
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                });
    }
}
