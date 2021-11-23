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
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.map(contactService.getContacts(id, txt), new TypeToken<List<ContactDto>>() {
        }.getType());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createContact(@RequestBody ContactDto contact) {
        contactService.createContact(mapper.map(contact, Contact.class));
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable String id) {
        contactService.deleteContact(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateContact(@RequestBody ContactDto contact) {
        contactService.updateContact(mapper.map(contact, Contact.class));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Contact contact = contactService.getContactById(id);
        contactService.partialUpdate(contact, updates);
    }
}