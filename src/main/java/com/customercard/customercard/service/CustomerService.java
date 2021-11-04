package com.customercard.customercard.service;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.repository.CustomerRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private CustomerRepo repo;
    private Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepo repo) {
        this.repo = repo;
    }

    public boolean createCustomer(Customer customer) {
        repo.save(customer);

        LOGGER.info("Customer added.");
        return true;
    }

    public boolean updateCustomer(Customer customer) {
        repo.save(customer);
        LOGGER.info("Customer updated.");
        return true;
    }

    public boolean deleteCustomer(String id) {
        repo.deleteById(id);
        LOGGER.info("Customer deleted.");
        return true;
    }

    public List<Customer> getAllCustomers() {
        LOGGER.info("Customers fetched.");
        return repo.findAll();
    }

    public Customer getById(String id) {
        LOGGER.info("Customer fetched by id.");
        return repo.findById(id).orElse(new Customer("",""));
    }

    public List<Customer> getByNameFragment(@Nullable String txt) {
        LOGGER.info("Customer fetched by text fragment.");
        return repo.findAll()
                .stream()
                .filter(customer -> StringUtils.containsIgnoreCase(customer.getName(), txt)
                        || StringUtils.containsIgnoreCase(customer.getSurname(), txt))
                .collect(Collectors.toList());
    }

    public List<Customer> getCustomers(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getById(id));
        } else if (txt != null) {
            return getByNameFragment(txt);
        } else {
            return getAllCustomers();
        }
    }

    public int getLashesWorkNumber(@NotNull Customer customer) {
        int numWorks = 0;

        if (customer.getLashesList() != null) {
            numWorks = customer.getLashesList().size();
        }

        return numWorks;
    }

    public LocalDate getLastWorkDate(@NotNull Customer customer) {
        LocalDate lastDate = null;

        if (customer.getLashesList() != null) {
            lastDate = customer
                    .getLashesList()
                    .stream()
                    .map(Lashes::getDate)
                    .reduce(LocalDate.MIN,
                            BinaryOperator.maxBy(Comparator.nullsLast(Comparator.naturalOrder())));
        }

        return lastDate;
    }

    public boolean partialUpdate(Customer customer, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            customer.setName((String) updates.get("name"));
        }
        if (updates.containsKey("surname")) {
            customer.setSurname((String) updates.get("surname"));
        }
        if (updates.containsKey("comment")) {
            customer.setComment((String) updates.get("comment"));
        }
        if (updates.containsKey("lashesList")) {
            //TODO check is it work fine..
            customer.setLashesList(List.of((Lashes) updates.get("lashesList")));
        }
        updateCustomer(customer);
        return true;
    }

}
