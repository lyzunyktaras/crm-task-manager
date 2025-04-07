package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
@Tag(name = "Client Controller", description = """
    Manages CRUD operations for client entities.
    It supports retrieving all clients, getting clients for a specific user, searching, creating, updating, and deleting clients.
    Additionally, it handles caching and associates clients with the current user upon creation.""")
public class ClientController {

  private final ClientService clientService;

  @Operation(
      summary = "Retrieve all clients",
      description = """
          Fetches a complete list of all client records. Uses caching to improve performance by avoiding repeated database queries.
          Returns a list of ClientDTO objects.""")
  @ApiResponse(responseCode = "200", description = "List of all clients successfully retrieved.")
  @GetMapping
  public ResponseEntity<List<ClientDTO>> findAll() {
    return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK);
  }

  @Operation(
      summary = "Retrieve clients for a specific user",
      description = """
          Fetches clients that are associated with the given user ID.
          The method uses caching with the user ID as the key to optimize data retrieval."""
  )
  @ApiResponse(responseCode = "200", description = "List of clients for the specified user successfully retrieved.")
  @GetMapping("/user/{id}")
  public ResponseEntity<List<ClientDTO>> findForUser(
      @Parameter(description = "Unique identifier of the user whose clients are to be retrieved.", example = "1")
      @PathVariable Long id) {
    return new ResponseEntity<>(clientService.findAllForUser(id), HttpStatus.OK);
  }

  @Operation(
      summary = "Search clients",
      description = """
          Searches for clients based on the provided search term.
          The 'searchTerm' query parameter can match parts of a client's name, email, or other attributes.
          Returns a list of ClientDTO objects that meet the search criteria."""
  )
  @ApiResponse(responseCode = "200", description = "List of clients matching the search term successfully retrieved.")
  @GetMapping("/search")
  public ResponseEntity<List<ClientDTO>> search(
      @Parameter(description = "Term used to search for clients. For example, 'Acme' can match the client name or other details.",
          example = "Acme")
      @RequestParam String searchTerm) {
    return new ResponseEntity<>(clientService.search(searchTerm), HttpStatus.OK);
  }

  @Operation(
      summary = "Create a new client",
      description = """
          Creates a new client record using the provided client data.
          The input data is validated, and the current authenticated user is automatically associated with the new client.
          Returns the created ClientDTO object.""")
  @ApiResponse(responseCode = "201", description = "Client successfully created.")
  @PostMapping
  public ResponseEntity<ClientDTO> create(
      @Parameter(description = "Client data for the new client, including mandatory fields like name, email, and phone.", required = true)
      @Valid @RequestBody ClientDTO clientDTO) {
    return new ResponseEntity<>(clientService.create(clientDTO), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update an existing client",
      description = """
          Updates an existing client record identified by the provided ID with the new client data.
          The method validates the input data and returns the updated ClientDTO object upon success.""")
  @ApiResponse(responseCode = "200", description = "Client successfully updated.")
  @PutMapping("/{id}")
  public ResponseEntity<ClientDTO> update(
      @Parameter(description = "Unique identifier of the client to be updated.", example = "4")
      @PathVariable Long id,
      @Parameter(description = "Updated client data. Must contain valid fields to update the client record.", required = true)
      @Valid @RequestBody ClientDTO clientDTO) {
    return new ResponseEntity<>(clientService.update(id, clientDTO), HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a client",
      description = """
          Deletes the client record identified by the given ID.
          Returns a 204 No Content status indicating the client has been successfully removed from the system.""")
  @ApiResponse(responseCode = "204", description = "Client successfully deleted.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Unique identifier of the client to be deleted.", example = "4")
      @PathVariable Long id) {
    clientService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
