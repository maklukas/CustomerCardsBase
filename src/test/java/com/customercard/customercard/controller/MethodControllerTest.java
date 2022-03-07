package com.customercard.customercard.controller;

import com.customercard.customercard.model.dto.MethodDto;
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
class MethodControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final MethodDto method;

    @MockBean
    private final MethodController methodController;

    @Autowired
    public MethodControllerTest(MockMvc mockMvc, ObjectMapper mapper, MethodController methodController) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.methodController = methodController;
        this.method = new MethodDto("testName");
        this.method.setId("testId");
    }

    @Test
    void shouldGetMethod() throws Exception {
        when(methodController.getMethod(Optional.empty(), Optional.empty())).thenReturn(List.of(method));

        mockMvc.perform(get("/api/methods"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].name", is("testName")));
    }

    @Test
    void shouldCreateMethod() throws Exception {

        //given
        String json = mapper.writeValueAsString(method);

        //then
        mockMvc.perform(post("/api/methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    void shouldDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/methods/testId"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldUpdateMethod() throws Exception {
        //given
        String json = mapper.writeValueAsString(method);

        //then
        mockMvc.perform(put("/api/methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldPartialUpdate() throws Exception {
        //given
        String json = mapper.writeValueAsString(method);

        //then
        mockMvc.perform(patch("/api/methods")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("id", "testId"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}