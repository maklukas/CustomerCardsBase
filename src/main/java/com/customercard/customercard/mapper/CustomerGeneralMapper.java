package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.model.dto.CustomerWork;
import com.customercard.customercard.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomerGeneralMapper {

    private final CustomerService customerService;

    @Autowired
    public CustomerGeneralMapper(CustomerService customerService) {
        this.customerService = customerService;
    }

    public CustomerGeneralDto mapModelToDto(Customer customer) {
        return new CustomerGeneralDto(
                customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customerService.getLastWorkDate(customer),
                customerService.getLashesWorkNumber(customer));
    }

    public List<CustomerGeneralDto> mapModelListToDtoList(List<Customer> customers) {
        return customers.stream()
                .map(this::mapModelToDto)
                .collect(Collectors.toList());
    }

    public static List<CustomerWork> mapModelToCustomerWorks(List<Customer> customers) {

        return customers.stream()
                .filter(customer -> customer.getLashesList() != null)
                .flatMap(customer -> customer.getLashesList().stream()
                        .filter(works -> works.getNextDate() != null)
                        .map(w ->
                                new CustomerWork(
                                        customer.getId(),
                                        customer.getName(),
                                        customer.getSurname(),
                                        w.getNextDate())))
                .collect(Collectors.toList());
    }
}
