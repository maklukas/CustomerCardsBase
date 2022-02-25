package com.customercard.customercard.controller;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.dto.CustomerDto;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.model.dto.CustomerWork;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final CustomerDto customer;

    @MockBean
    private final CustomerController customerController;

    @Autowired
    CustomerControllerTest(MockMvc mockMvc,
                           ObjectMapper mapper,
                           CustomerController customerController) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.customerController = customerController;
        this.customer = new CustomerDto();
        this.customer.setId("testId");
        this.customer.setName("testName");
        this.customer.setComment("testComment");
        this.customer.setSurname("testSurname");
    }


    @Test
    void shouldGetCustomer() throws Exception {

        when(customerController.getCustomer(Optional.empty(), Optional.empty())).thenReturn(List.of(this.customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].name", is("testName")))
                .andExpect(jsonPath("$[0].surname", is("testSurname")))
                .andExpect(jsonPath("$[0].comment", is("testComment")));
    }

    @Test
    void shouldGetCustomerGeneral() throws Exception {
        List<CustomerGeneralDto> customerGenerals = new ArrayList<>();
        customerGenerals.add(new CustomerGeneralDto(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                null,
                0
        ));

        when(customerController.getCustomerGeneral(Optional.empty(), Optional.empty())).thenReturn(customerGenerals);

        mockMvc.perform(get("/api/customers/general"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].name", is("testName")))
                .andExpect(jsonPath("$[0].surname", is("testSurname")))
                .andExpect(jsonPath("$[0].totalWorks", is(0)));
    }

    @Test
    void shouldGetCustomerNext() throws Exception {

        CustomerWork customerWork = new CustomerWork();
        customerWork.setId(customer.getId());
        customerWork.setName(customer.getName());
        customerWork.setSurname(customer.getSurname());
        customerWork.setDate(LocalDateTime.now());

        when(customerController.getCustomerNext("testId",  "")).thenReturn(List.of(customerWork));

        mockMvc.perform(get("/api/customers/next"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].name", is("testName")))
                .andExpect(jsonPath("$[0].surname", is("testSurname")))
                .andExpect(jsonPath("$[0].date", is(LocalDateTime.now())));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        String customerJson = mapper.writeValueAsString(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/testId"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        String customerJson = mapper.writeValueAsString(customer);

        mockMvc.perform(put("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPartialUpdate() throws Exception {
        String customerJson = mapper.writeValueAsString(customer);

        mockMvc.perform(patch("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson)
                        .param("id", "testId"))
                .andExpect(status().isOk());
    }
}