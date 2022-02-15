package com.customercard.customercard.controller;


import com.customercard.customercard.model.Customer;
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
import java.util.Optional;

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
            @RequestParam(required = false, value = "id") Optional<String> id,
            @RequestParam(required = false, value = "txt") Optional<String> txt) {

        List<Method> methods = id.map(val -> List.of(methodService.getById(val)))
                .orElse(txt.map(methodService::getByName)
                        .orElse(methodService.getAll()));

        return mapper.map(methods, new TypeToken<List<MethodDto>>() {}.getType());
    }

    @PostMapping
    public void createMethod(@RequestBody MethodDto method) {
        methodService.create(mapper.map(method, Method.class));
    }

    @DeleteMapping("/{id}")
    public void deleteMethod(@PathVariable String id) {
        methodService.delete(id);
    }

    @PutMapping
    public void updateMethod(@RequestBody MethodDto method) {
        methodService.update(mapper.map(method, Method.class));
    }

    @PatchMapping(params = "id")
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Method method = methodService.getById(id);
        methodService.partialUpdate(method, updates);
    }
}