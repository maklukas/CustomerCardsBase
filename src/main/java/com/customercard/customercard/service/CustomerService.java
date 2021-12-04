package com.customercard.customercard.service;

import com.customercard.customercard.model.Contact;
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
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Service("customerService")
public class CustomerService {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepo repo;
    private final LashesService lashesService;
    private final ContactService contactService;

    @Autowired
    public CustomerService(CustomerRepo repo, LashesService lashesService, ContactService contactService) {
        this.repo = repo;
        this.lashesService = lashesService;
        this.contactService = contactService;
    }

    public Customer create(Customer customer) {
        LOGGER.info("Customer added.");
        createSubClasses(customer);
        return repo.save(customer);
    }

    public Customer update(Customer customer) {
        createSubClasses(customer);
        LOGGER.info("Customer updated.");
        return repo.save(customer);
    }

    public boolean delete(String id) {
        repo.deleteById(id);
        LOGGER.info("Customer deleted.");
        return true;
    }

    public List<Customer> getAll() {
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

    public List<Customer> getAll(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getById(id));
        } else if (txt != null) {
            return getByNameFragment(txt);
        } else {
            return getAll();
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

        if (customer.getLashesList() != null && customer.getLashesList().size() > 0) {
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
        if (updates.containsKey("contact")) {
            customer.setContact((Contact) updates.get("contact"));
        }
        if (updates.containsKey("lashesList")) {
            customer.setLashesList(List.of((Lashes) updates.get("lashesList")));
        }
        update(customer);
        return true;
    }

    private void createSubClasses(Customer customer) {
        if (customer.getLashesList() != null) {
            createLashesList(customer);
        }
        if (customer.getContact() != null) {
            createContact(customer);
        }
    }

    private void createLashesList(Customer customer) {
        List<Lashes> lashesList = customer.getLashesList();
        List<Lashes> newList = new ArrayList<>();

        if (lashesList.size() > 0) {
            for (Lashes theLash: lashesList) {
                newList.add(lashesService.create(theLash));
            }
        }

        customer.setLashesList(newList);
    }

    private void createContact(Customer customer) {
        if (customer.getContact() != null) {
            customer.setContact(contactService.create(customer.getContact()));
        }
    }

}
