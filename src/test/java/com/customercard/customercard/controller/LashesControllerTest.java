package com.customercard.customercard.controller;

import com.customercard.customercard.model.dto.LashesDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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
class LashesControllerTest {

    private final MockMvc mockMvc;
    private final LashesDto lashes;
    private final ObjectMapper mapper;

    @MockBean
    private final LashesController lashesController;

    @Autowired
    public LashesControllerTest(MockMvc mockMvc, LashesController lashesController, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.lashesController = lashesController;
        this.mapper = mapper;
        this.lashes = new LashesDto();
        initObject();
    }

    private void initObject() {
        this.lashes.setId("testId");
        this.lashes.setColor("testColor");
        this.lashes.setComment("testComment");
        this.lashes.setDate(LocalDate.of(2020,1,1).atStartOfDay());
        this.lashes.setMethod("testMethod");
        this.lashes.setNextDate(this.lashes.getDate().plusMonths(1));
        this.lashes.setStyle("testStyle");
    }

    @Test
    void shouldGetLashes() throws Exception {
        when(lashesController.getLashes(Optional.empty(), Optional.empty())).thenReturn(List.of(lashes));

        mockMvc.perform(get("/api/lashes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("testId")))
                .andExpect(jsonPath("$[0].color", is("testColor")))
                .andExpect(jsonPath("$[0].style", is("testStyle")))
                .andExpect(jsonPath("$[0].method", is("testMethod")))
                .andExpect(jsonPath("$[0].comment", is("testComment")));
    }

    @Test
    void shouldCreateLashes() throws Exception {

        //given
        String json = mapper.writeValueAsString(lashes);

        //then
        mockMvc.perform(post("/api/lashes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldDeleteLashes() throws Exception {

        mockMvc.perform(delete("/api/lashes/testId"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldUpdateLashes() throws Exception {

        //given
        String json = mapper.writeValueAsString(lashes);

        //then
        mockMvc.perform(put("/api/lashes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldPartialUpdate() throws Exception {

        String json = mapper.writeValueAsString(lashes);

        mockMvc.perform(patch("/api/lashes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .param("id", "testId"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}