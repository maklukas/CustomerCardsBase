package com.customercard.customercard.controller;


import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.dto.MethodDto;
import com.customercard.customercard.service.MethodService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/methods")
@CrossOrigin("*")
public class MethodController {

    private final MethodService methodService;
    private final ModelMapper mapper;

    @Autowired
    public MethodController(MethodService methodService, ModelMapper mapper) {
        this.methodService = methodService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<MethodDto> getMethod(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.map(methodService.getAll(id, txt), new TypeToken<List<MethodDto>>() {}.getType());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createMethod(@RequestBody MethodDto method) {
        methodService.create(mapper.map(method, Method.class));
    }

    @DeleteMapping("/{id}")
    public void deleteMethod(@PathVariable String id) {
        methodService.delete(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMethod(@RequestBody MethodDto method) {
        methodService.update(mapper.map(method, Method.class));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Method method = methodService.getById(id);
        methodService.partialUpdate(method, updates);
    }
}