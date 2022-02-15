package com.customercard.customercard.controller;


import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.dto.ContactDto;
import com.customercard.customercard.service.ContactService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin("*")
public class ContactController {

    private final ContactService contactService;
    private final ModelMapper mapper;

    @Autowired
    public ContactController(ContactService contactService, ModelMapper mapper) {
        this.contactService = contactService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ContactDto> getContact(
            @RequestParam(required = false, value = "id") Optional<String> id,
            @RequestParam(required = false, value = "txt") Optional<String> txt) {
        List<Contact> contacts = id.map(val -> List.of(contactService.getById(val)))
                .orElse(txt.map(contactService::getByName)
                        .orElse(contactService.getAll()));
        return mapper.map(contacts, new TypeToken<List<ContactDto>>() {
        }.getType());
    }

    @PostMapping
    public void createContact(@RequestBody ContactDto contact) {
        contactService.create(mapper.map(contact, Contact.class));
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable String id) {
        contactService.delete(id);
    }

    @PutMapping
    public void updateContact(@RequestBody ContactDto contact) {
        contactService.update(mapper.map(contact, Contact.class));
    }

    @PatchMapping(params = "id")
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Contact contact = contactService.getById(id);
        contactService.partialUpdate(contact, updates);
    }
}