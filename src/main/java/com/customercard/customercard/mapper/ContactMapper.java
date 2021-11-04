package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.dto.ContactDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactMapper {

    private final ModelMapper mapper;

    @Autowired
    public ContactMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Contact mapDtoToModel(ContactDto contact) {
        return mapper.map(contact, Contact.class);
    }

    public ContactDto mapModelToDto(Contact contact) {
        return mapper.map(contact, ContactDto.class);
    }

    public List<Contact> mapDtoListToModelList(List<ContactDto> contacts) {
        return contacts.stream()
                .map(contact -> mapper.map(contact, Contact.class))
                .collect(Collectors.toList());
    }

    public List<ContactDto> mapModelListToDtoList(List<Contact> contacts) {
        return contacts.stream()
                .map(contact -> mapper.map(contact, ContactDto.class))
                .collect(Collectors.toList());
    }
}
