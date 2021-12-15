package com.customercard.customercard.controller;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.dto.CustomerDto;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.model.dto.CustomerWork;
import com.customercard.customercard.service.CustomerService;
import com.googlecode.gentyref.TypeToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper mapper;
    private final CustomerGeneralMapper customerGeneralMapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper mapper, CustomerGeneralMapper customerGeneralMapper) {
        this.customerService = customerService;
        this.mapper = mapper;
        this.customerGeneralMapper = customerGeneralMapper;
    }

    @GetMapping
    public List<CustomerDto> getCustomer(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.map(customerService.getAll(id, txt), new TypeToken<List<CustomerDto>>() {
        }.getType());
    }

    @GetMapping("/general")
    public List<CustomerGeneralDto> getCustomerGeneral(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return customerGeneralMapper.mapModelListToDtoList(customerService.getAll(id, txt));
    }

    @GetMapping("/next")
    public List<CustomerWork> getCustomerNext(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "name") String name) {
        return customerService.getNextWeekWorks(id, name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCustomer(@RequestBody CustomerDto customer) {
        customerService.create(mapper.map(customer, Customer.class));
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        customerService.delete(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCustomer(@RequestBody CustomerDto customer) {
        customerService.update(mapper.map(customer, Customer.class));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Customer customer = customerService.getById(id);
        customerService.partialUpdate(customer, updates);
    }
}
