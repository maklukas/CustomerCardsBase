package com.customercard.customercard.controller;


import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.dto.ColorDto;
import com.customercard.customercard.service.ColorService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin("*")
public class ColorController {

    private final ColorService colorService;
    private final ModelMapper mapper;

    @Autowired
    public ColorController(ColorService colorService, ModelMapper mapper) {
        this.colorService = colorService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ColorDto> getColor(
            @RequestParam(required = false, value = "id") Optional<String> id,
            @RequestParam(required = false, value = "txt") Optional<String> txt) {

        List<Color> colors = id.map(val -> List.of(colorService.getById(val)))
                .orElse(txt.map(colorService::getByName)
                        .orElse(colorService.getAll()));

        return mapper.map(colors, new TypeToken<List<ColorDto>>() {}.getType());
    }

    @PostMapping
    public void createColor(@RequestBody ColorDto color) {
        colorService.create(mapper.map(color, Color.class));
    }

    @DeleteMapping("/{id}")
    public void deleteColor(@PathVariable String id) {
        colorService.delete(id);
    }

    @PutMapping
    public void updateColor(@RequestBody ColorDto color) {
        colorService.update(mapper.map(color, Color.class));
    }

    @PatchMapping(params = "id")
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Color color = colorService.getById(id);
        colorService.partialUpdate(color, updates);
    }
}