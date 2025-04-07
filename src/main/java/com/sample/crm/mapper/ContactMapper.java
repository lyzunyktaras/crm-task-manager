package com.sample.crm.mapper;

import com.sample.crm.dto.ContactDTO;
import com.sample.crm.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    ContactDTO toDto(Contact contact);

    Contact toEntity(ContactDTO contactDTO);

    @Mapping(target = "client", ignore = true)
    void update(@MappingTarget Contact contact, ContactDTO contactDTO);
}
