package com.customercard.customercard.controller;


import com.customercard.customercard.model.Customer;
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
import java.util.Optional;

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
            @RequestParam(required = false, value = "id") Optional<String> id,
            @RequestParam(required = false, value = "txt") Optional<String> txt) {

        List<Lashes> lashes = id.map(val -> List.of(lashesService.getById(val)))
                .orElse(txt.map(lashesService::getByComment)
                        .orElse(lashesService.getAll()));

        return mapper.map(lashes, new TypeToken<List<LashesDto>>() {
        }.getType());
    }

    @PostMapping
    public void createLashes(@RequestBody LashesDto lashes) {
        lashesService.create(mapper.map(lashes, Lashes.class));
    }

    @DeleteMapping("/{id}")
    public void deleteLashes(@PathVariable String id) {
        lashesService.delete(id);
    }

    @PutMapping
    public void updateLashes(@RequestBody LashesDto lashes) {
        lashesService.update(mapper.map(lashes, Lashes.class));
    }

    @PatchMapping(params = "id")
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Lashes lashes = lashesService.getById(id);
        lashesService.partialUpdate(lashes, updates);
    }
}