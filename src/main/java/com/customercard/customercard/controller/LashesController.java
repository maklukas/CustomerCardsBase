package com.customercard.customercard.controller;


import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.LashesDto;
import com.customercard.customercard.service.LashesService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lashes")
@CrossOrigin("*")
public class LashesController {

    private final LashesService lashesService;
    private final ModelMapper mapper;

    @Autowired
    public LashesController(LashesService lashesService, ModelMapper mapper) {
        this.lashesService = lashesService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<LashesDto> getLashes(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.map(lashesService.getLashes(id, txt), new TypeToken<List<LashesDto>>() {
        }.getType());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createLashes(@RequestBody LashesDto lashes) {
        lashesService.createLashes(mapper.map(lashes, Lashes.class));
    }

    @DeleteMapping("/{id}")
    public void deleteLashes(@PathVariable String id) {
        lashesService.deleteLashes(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateLashes(@RequestBody LashesDto lashes) {
        lashesService.updateLashes(mapper.map(lashes, Lashes.class));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Lashes lashes = lashesService.getLashesById(id);
        lashesService.partialUpdate(lashes, updates);
    }
}