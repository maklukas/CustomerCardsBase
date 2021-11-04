package com.customercard.customercard.controller;


import com.customercard.customercard.mapper.ColorMapper;
import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.dto.ColorDto;
import com.customercard.customercard.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin("*")
public class ColorController {

    private final ColorService colorService;
    private final ColorMapper mapper;

    @Autowired
    public ColorController(ColorService colorService, ColorMapper mapper) {
        this.colorService = colorService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ColorDto> getColor(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.mapModelListToDtoList(colorService.getColors(id, txt));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createColor(@RequestBody ColorDto color) {
        colorService.createColor(mapper.mapDtoToModel(color));
    }

    @DeleteMapping("/{id}")
    public void deleteColor(@PathVariable String id) {
        colorService.deleteColor(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateColor(@RequestBody ColorDto color) {
        colorService.updateColor(mapper.mapDtoToModel(color));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Color color = colorService.getColorById(id);
        colorService.partialUpdate(color, updates);
    }
}