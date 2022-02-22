package com.customercard.customercard.controller;

import com.customercard.customercard.model.dto.ColorDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ColorControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final ColorController colorController;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ColorControllerTest(MockMvc mockMvc, ColorController colorController) {
        this.mockMvc = mockMvc;
        this.colorController = colorController;
    }

    @Test
    void shouldGetColors() throws Exception {
        ColorDto color = new ColorDto();
        color.setId("test");
        color.setName("test2");

        when(colorController.getColor(Optional.empty(), Optional.empty()))
                .thenReturn(List.of(color));

        mockMvc
                .perform(get("/api/colors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("test")))
                .andExpect(jsonPath("$[0].name", is("test2")));
    }

    @Test
    void shouldCreateColor() throws Exception {
        ColorDto color = new ColorDto();
        color.setId("test");
        color.setName("test2");

        String colorJson = mapper.writeValueAsString(color);

        mockMvc
                .perform(post("/api/colors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(colorJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteColor() throws Exception {
        mockMvc
                .perform(delete("/api/colors/test"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateColor() throws Exception {
        ColorDto color = new ColorDto();
        color.setId("test");
        color.setName("test2");

        String colorJson = mapper.writeValueAsString(color);

        mockMvc
                .perform(put("/api/colors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(colorJson))
                .andExpect(status().isOk());
    }

    @Test
    void shouldPartialUpdateColor() throws Exception {
        ColorDto color = new ColorDto();
        color.setId("test");
        color.setName("test2");

        String colorJson = mapper.writeValueAsString(color);

        mockMvc
                .perform(patch("/api/colors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", color.getId())
                        .content(colorJson))
                .andExpect(status().isOk());
    }
}