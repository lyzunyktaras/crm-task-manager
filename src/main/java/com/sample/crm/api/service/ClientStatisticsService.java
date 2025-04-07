package com.sample.crm.api.service;

import com.sample.crm.api.dto.ClientStatisticsDTO;
import com.sample.crm.entity.Client;
import com.sample.crm.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClientStatisticsService {
    private final ClientRepository clientRepository;

    public ClientStatisticsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientStatisticsDTO getClientStatistics() {
        long totalClients = clientRepository.count();
        Map<String, Long> clientsByIndustry = clientRepository.findAll().stream()
                .collect(Collectors.groupingBy(Client::getIndustry, Collectors.counting()));

        return new ClientStatisticsDTO(totalClients, clientsByIndustry);
    }
}
