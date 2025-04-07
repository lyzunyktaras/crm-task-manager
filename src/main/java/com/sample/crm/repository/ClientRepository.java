package com.sample.crm.repository;

import com.sample.crm.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("""
            SELECT c FROM Client c
            JOIN c.users u WHERE u.id = :userId
            """)
    List<Client> findAllByUserId(Long userId);

    @Query("""
        SELECT c FROM Client c
        WHERE (:searchTerm IS NULL OR LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
          OR (:searchTerm IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
          OR (:searchTerm IS NULL OR LOWER(c.industry) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    """)
    List<Client> search(String searchTerm);
}
