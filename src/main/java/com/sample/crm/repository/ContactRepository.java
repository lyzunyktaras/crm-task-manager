package com.sample.crm.repository;

import com.sample.crm.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("""
    SELECT c FROM Contact c
    WHERE (:clientId IS NULL OR c.client.id = :clientId)
      AND (
          :searchTerm IS NULL
          OR LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          OR LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
      )
""")
    List<Contact> search(Long clientId, String searchTerm);
}
