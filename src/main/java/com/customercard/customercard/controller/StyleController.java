package com.customercard.customercard.controller;


import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.Style;
import com.customercard.customercard.model.dto.StyleDto;
import com.customercard.customercard.service.StyleService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/styles")
@CrossOrigin("*")
public class StyleController {

    private final StyleService styleService;
    private final ModelMapper mapper;

    @Autowired
    public StyleController(StyleService styleService, ModelMapper mapper) {
        this.styleService = styleService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<StyleDto> getStyle(
            @RequestParam(required = false, value = "id") Optional<String> id,
            @RequestParam(required = false, value = "txt") Optional<String> txt) {

        List<Style> styles = id.map(val -> List.of(styleService.getById(val)))
                .orElse(txt.map(styleService::getByName)
                        .orElse(styleService.getAll()));

        return mapper.map(styles, new TypeToken<List<StyleDto>>() {}.getType());
    }

    @PostMapping
    public void createStyle(@RequestBody StyleDto style) {
        styleService.create(mapper.map(style, Style.class));
    }

    @DeleteMapping("/{id}")
    public void deleteStyle(@PathVariable String id) {
        styleService.delete(id);
    }

    @PutMapping
    public void updateStyle(@RequestBody StyleDto style) {
        styleService.update(mapper.map(style, Style.class));
    }

    @PatchMapping(params = "id")
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Style style = styleService.getById(id);
        styleService.partialUpdate(style, updates);
    }
}