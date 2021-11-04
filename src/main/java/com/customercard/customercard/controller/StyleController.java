package com.customercard.customercard.controller;


import com.customercard.customercard.mapper.StyleMapper;
import com.customercard.customercard.model.Style;
import com.customercard.customercard.model.dto.StyleDto;
import com.customercard.customercard.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/styles")
@CrossOrigin("*")
public class StyleController {

    private final StyleService styleService;
    private final StyleMapper mapper;

    @Autowired
    public StyleController(StyleService styleService, StyleMapper mapper) {
        this.styleService = styleService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<StyleDto> getStyle(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.mapModelListToDtoList(styleService.getStyles(id, txt));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createStyle(@RequestBody StyleDto style) {
        styleService.createStyle(mapper.mapDtoToModel(style));
    }

    @DeleteMapping("/{id}")
    public void deleteStyle(@PathVariable String id) {
        styleService.deleteStyle(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateStyle(@RequestBody StyleDto style) {
        styleService.updateStyle(mapper.mapDtoToModel(style));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Style style = styleService.getStyleById(id);
        styleService.partialUpdate(style, updates);
    }
}