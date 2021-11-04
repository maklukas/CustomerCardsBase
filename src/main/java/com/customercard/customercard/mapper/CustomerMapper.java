package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.dto.CustomerDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private final ModelMapper mapper;

    @Autowired
    public CustomerMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Customer mapDtoToModel(CustomerDto customer) {
        return mapper.map(customer, Customer.class);
    }

    public CustomerDto mapModelToDto(Customer customer) {
        return mapper.map(customer, CustomerDto.class);
    }

    public List<Customer> mapDtoListToModelList(List<CustomerDto> customers) {
        return customers.stream()
                .map(customer -> mapper.map(customer, Customer.class))
                .collect(Collectors.toList());
    }

    public List<CustomerDto> mapModelListToDtoList(List<Customer> customers) {
        return customers.stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());
    }
}
