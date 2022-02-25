package com.customercard.customercard.controller;

import com.customercard.customercard.model.dto.ContactDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final ContactDto contact;

    @MockBean
    private final ContactController contactController;

    @Autowired
    public ContactControllerTest(ContactController contactController, MockMvc mockMvc, ObjectMapper mapper) {
        this.contactController = contactController;
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.contact = new ContactDto();
        this.contact.setId("testId");
        this.contact.setCity("testCity");
        this.contact.setBoxOffice("testBox");
        this.contact.setEmailAddress("testEmail");
        this.contact.setPhoneNumber("testPhone");
        this.contact.setStreet("testStreet");
    }

    @Test
    void shouldGetContact() throws Exception {

        ContactDto contact2 = new ContactDto();
        contact2.setId("testId2");
        contact2.setCity("testCity");
        contact2.setBoxOffice("testBox");
        contact2.setEmailAddress("testEmail");
        contact2.setPhoneNumber("testPhone");
        contact2.setStreet("testStreet");

        when(contactController.getContact(Optional.empty(), Optional.empty())).thenReturn(List.of(contact, contact2));
        when(contactController.getContact(Optional.of("testId2"), Optional.empty())).thenReturn(List.of(contact2));

        mockMvc.perform(get("/api/contacts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].city", is("testCity")))
                .andExpect(jsonPath("$[0].boxOffice", is("testBox")))
                .andExpect(jsonPath("$[0].emailAddress", is("testEmail")))
                .andExpect(jsonPath("$[0].phoneNumber", is("testPhone")))
                .andExpect(jsonPath("$[0].street", is("testStreet")));
    }

    @Test
    void shouldCreateContact() throws Exception {

        String contactJson = mapper.writeValueAsString(contact);

        mockMvc.perform(post("/api/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactJson))
            .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteContact() throws Exception {
        mockMvc.perform(delete("/api/contacts/test"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateContact() throws Exception {

        String contactJson = mapper.writeValueAsString(contact);

        mockMvc.perform(put("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPartialUpdate() throws Exception {

        String contactJson = mapper.writeValueAsString(contact);

        mockMvc.perform(patch("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "test")
                        .content(contactJson))
                .andExpect(status().isOk());
    }
}