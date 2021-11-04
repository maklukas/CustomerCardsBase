package com.customercard.customercard.controller;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.mapper.CustomerMapper;
import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.dto.CustomerDto;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.service.CustomerService;
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
    private final CustomerMapper mapper;
    private final CustomerGeneralMapper customerGeneralMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper mapper, CustomerGeneralMapper customerGeneralMapper) {
        this.customerService = customerService;
        this.mapper = mapper;
        this.customerGeneralMapper = customerGeneralMapper;
    }

    @GetMapping
    public List<CustomerDto> getCustomer(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return mapper.mapModelListToDtoList(customerService.getCustomers(id, txt));
    }

    @GetMapping("/general")
    public List<CustomerGeneralDto> getCustomerGeneral(
            @RequestParam(required = false, value = "id") String id,
            @RequestParam(required = false, value = "txt") String txt) {
        return customerGeneralMapper.mapModelListToDtoList(customerService.getCustomers(id, txt));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createCustomer(@RequestBody CustomerDto customer) {
        customerService.createCustomer(mapper.mapDtoToModel(customer));
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateCustomer(@RequestBody CustomerDto customer) {
        customerService.updateCustomer(mapper.mapDtoToModel(customer));
    }

    @PatchMapping(params = "id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void partialUpdate(@RequestParam String id, @RequestBody Map<String, Object> updates) {
        Customer customer = customerService.getById(id);
        customerService.partialUpdate(customer, updates);
    }
}
