package com.customercard.customercard.controller;

import com.customercard.customercard.model.dto.StyleDto;
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
class StyleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;
    private final StyleDto style;

    @MockBean
    private final StyleController styleController;

    @Autowired
    public StyleControllerTest(MockMvc mockMvc, ObjectMapper mapper, StyleController styleController) {
        this.mockMvc = mockMvc;
        this.mapper = mapper;
        this.styleController = styleController;
        this.style = new StyleDto("testName");
        this.style.setId("testId");
    }

    @Test
    void shouldGetStyle() throws Exception {
        when(styleController.getStyle(Optional.empty(), Optional.empty())).thenReturn(List.of(style));

        mockMvc.perform(get("/api/styles"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].name", is("testName")));
    }

    @Test
    void shouldCreateStyle() throws Exception {
        String json = mapper.writeValueAsString(style);

        mockMvc.perform(post("/api/styles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldDeleteStyle() throws Exception {
        mockMvc.perform(delete("/api/styles/testId"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldUpdateStyle() throws Exception {
        String json = mapper.writeValueAsString(style);

        mockMvc.perform(put("/api/styles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldPartialUpdate() throws Exception {

        String json = mapper.writeValueAsString(style);

        mockMvc.perform(patch("/api/styles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("id", "testId"))
                .andExpect(status().isOk())
                .andDo(print());

    }
}