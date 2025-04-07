package com.sample.crm.controller;

import java.util.List;

import com.sample.crm.dto.ContactDTO;
import com.sample.crm.service.ContactService;
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
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Tag(name = "Contact Controller", description = """
    Manages operations for contact entities.
    It supports retrieving all contacts, searching contacts by client ID and search term,
    creating a new contact, updating an existing contact, and deleting a contact.
    Additionally, it handles caching to optimize data retrieval and updates.""")
public class ContactController {

  private final ContactService contactService;

  @Operation(
      summary = "Retrieve all contacts",
      description = """
          Fetches a complete list of all contact records.
          Uses caching to improve performance by reducing database queries.
          Returns a list of ContactDTO objects."""
  )
  @ApiResponse(responseCode = "200", description = "List of all contacts successfully retrieved.")
  @GetMapping
  public ResponseEntity<List<ContactDTO>> findAll() {
    return new ResponseEntity<>(contactService.findAll(), HttpStatus.OK);
  }

  @Operation(
      summary = "Search contacts",
      description = """
          Searches for contacts based on the provided client ID and search term.
          If clientId is provided, the search is restricted to contacts belonging to that client.
          The searchTerm can match parts of a contact's name, email, or other attributes.
          Returns a list of ContactDTO objects that match the criteria."""
  )
  @ApiResponse(responseCode = "200", description = "List of contacts matching the search criteria successfully retrieved.")
  @GetMapping("/search")
  public ResponseEntity<List<ContactDTO>> search(
      @Parameter(description = "Identifier of the client to search within. Optional.", example = "1")
      @RequestParam(required = false) Long clientId,
      @Parameter(description = "Search term for filtering contacts. Example: 'Term'", example = "Term")
      @RequestParam(defaultValue = "") String searchTerm) {
    return new ResponseEntity<>(contactService.search(clientId, searchTerm), HttpStatus.OK);
  }

  @Operation(
      summary = "Create a new contact",
      description = """
          Creates a new contact record using the provided contact data.
          The input data is validated, and upon successful creation, returns the created ContactDTO object.
          Caching is cleared to ensure data consistency."""
  )
  @ApiResponse(responseCode = "201", description = "Contact successfully created.")
  @PostMapping
  public ResponseEntity<ContactDTO> create(
      @Parameter(description = "Contact data for the new contact, including required fields like name, email, and phone.", required = true)
      @Valid @RequestBody ContactDTO contactDTO) {
    return new ResponseEntity<>(contactService.create(contactDTO), HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update an existing contact",
      description = """
          Updates an existing contact identified by the provided ID with the new contact data.
          The input data is validated, and the contact is updated accordingly.
          Caching is cleared to ensure updated data is reflected.
          Returns the updated ContactDTO object."""
  )
  @ApiResponse(responseCode = "200", description = "Contact successfully updated.")
  @PutMapping("/{id}")
  public ResponseEntity<ContactDTO> update(
      @Parameter(description = "Unique identifier of the contact to be updated.", example = "4")
      @PathVariable Long id,
      @Parameter(description = "Updated contact data with valid fields to modify the contact record.", required = true)
      @Valid @RequestBody ContactDTO contactDTO) {
    return new ResponseEntity<>(contactService.update(id, contactDTO), HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a contact",
      description = """
          Deletes the contact identified by the provided ID.
          After deletion, returns a 204 No Content status indicating the contact has been successfully removed.
          Caching is cleared to maintain data consistency."""
  )
  @ApiResponse(responseCode = "204", description = "Contact successfully deleted.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Unique identifier of the contact to be deleted.", example = "4")
      @PathVariable Long id) {
    contactService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
