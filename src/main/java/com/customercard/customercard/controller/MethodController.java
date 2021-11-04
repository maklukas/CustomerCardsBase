package com.customercard.customercard.controller;


import com.customercard.customercard.mapper.MethodMapper;
import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.dto.MethodDto;
import com.customercard.customercard.service.MethodService;
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
    private final MethodMapper mapper;

    @Autowired
    public MethodController(MethodService methodService, MethodMapper mapper) {
        this.methodService = methodService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<MethodDto> getMethod(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.mapModelListToDtoList(methodService.getMethods(id, txt));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createMethod(@RequestBody MethodDto method) {
        methodService.createMethod(mapper.mapDtoToModel(method));
    }

    @DeleteMapping("/{id}")
    public void deleteMethod(@PathVariable String id) {
        methodService.deleteMethod(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateMethod(@RequestBody MethodDto method) {
        methodService.updateMethod(mapper.mapDtoToModel(method));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Method method = methodService.getMethodById(id);
        methodService.partialUpdate(method, updates);
    }
}