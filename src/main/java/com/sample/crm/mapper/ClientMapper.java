package com.sample.crm.mapper;

import com.sample.crm.dto.ClientDTO;
import com.sample.crm.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "users", ignore = true)
    ClientDTO toDto(Client client);

    Client toEntity(ClientDTO clientDTO);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Client client, ClientDTO clientDTO);
}
