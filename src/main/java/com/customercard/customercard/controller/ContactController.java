package com.customercard.customercard.controller;


import com.customercard.customercard.mapper.ContactMapper;
import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.dto.ContactDto;
import com.customercard.customercard.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin("*")
public class ContactController {

    private final ContactService contactService;
    private final ContactMapper mapper;

    @Autowired
    public ContactController(ContactService contactService, ContactMapper mapper) {
        this.contactService = contactService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ContactDto> getContact(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.mapModelListToDtoList(contactService.getContacts(id, txt));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createContact(@RequestBody ContactDto contact) {
        contactService.createContact(mapper.mapDtoToModel(contact));
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateContact(@RequestBody ContactDto contact) {
        contactService.updateContact(mapper.mapDtoToModel(contact));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Contact contact = contactService.getContactById(id);
        contactService.partialUpdate(contact, updates);
    }
}