package com.sample.crm.service;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.exception.NotFoundException;
import com.sample.crm.exception.model.ExceptionMessage;
import com.sample.crm.mapper.ClientMapper;
import com.sample.crm.mapper.UserMapper;
import com.sample.crm.repository.ClientRepository;
import com.sample.crm.repository.UserRepository;
import com.sample.crm.util.SecurityUtil;
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
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Retrieves all clients, with caching enabled.
     *
     * @return List of all clients as ClientDTO.
     */
    @Cacheable(value = "clients")
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        log.debug("Fetching all clients");
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    /**
     * Retrieves all clients associated with a specific user, with caching enabled.
     *
     * @param id User ID.
     * @return List of clients for the specified user as ClientDTO.
     */
    @Cacheable(value = "clients", key = "#id")
    @Transactional(readOnly = true)
    public List<ClientDTO> findAllForUser(Long id) {
        log.debug("Fetching clients for user with ID: {}", id);
        return clientRepository.findAllByUserId(id).stream().map(clientMapper::toDto).toList();
    }

    /**
     * Searches clients based on the search term.
     *
     * @param searchTerm The term to search for.
     * @return List of matching clients as ClientDTO.
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> search(String searchTerm) {
        log.debug("Searching clients with term: {}", searchTerm);
        return clientRepository.search(searchTerm).stream().map(clientMapper::toDto).toList();
    }

    /**
     * Creates a new client and clears the cache.
     *
     * @param clientDTO The client data to create.
     * @return The created client as ClientDTO.
     */
    @Transactional
    @CacheEvict(value = "clients", allEntries = true)
    public ClientDTO create(ClientDTO clientDTO) {
        log.debug("Creating a new client: {}", clientDTO);
        Client client = clientMapper.toEntity(clientDTO);
        client.setUsers(List.of(userRepository.findByUsername(SecurityUtil.getCurrentUser().getUsername())
                .orElseThrow(() -> {
                    log.error("Current user not found for creating client");
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                })));
        return clientMapper.toDto(clientRepository.save(client));
    }

    /**
     * Updates an existing client and clears the cache.
     *
     * @param id        The ID of the client to update.
     * @param clientDTO The updated client data.
     * @return The updated client as ClientDTO.
     */
    @Transactional
    @CacheEvict(value = "clients", allEntries = true)
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        log.debug("Updating client with ID: {}", id);
        Client client = getClient(id);
        clientMapper.update(client, clientDTO);
        return clientMapper.toDto(clientRepository.save(client));
    }

    /**
     * Deletes a client by ID and clears the cache.
     *
     * @param id The ID of the client to delete.
     */
    @CacheEvict(value = "clients", allEntries = true)
    public void delete(Long id) {
        log.debug("Deleting client with ID: {}", id);
        clientRepository.deleteById(id);
    }

    /**
     * Retrieves a client entity by ID.
     *
     * @param id The ID of the client to retrieve.
     * @return The client entity.
     * @throws NotFoundException if the client is not found.
     */
    private Client getClient(Long id) {
        log.debug("Fetching client with ID: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client not found with ID: {}", id);
                    return new NotFoundException(ExceptionMessage.NOT_FOUND);
                });
    }
}
