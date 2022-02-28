package com.customercard.customercard.service;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.CustomerWork;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerServiceTest {

    private final CustomerService service;
    private final LashesService lashesService;
    private Customer customer;
    private final LocalDateTime timeNow;
    private final long collectionSize;
    private final int PLUS_MONTHS = 99;

    @Autowired
    public CustomerServiceTest(CustomerService service, LashesService lashesService) {
        this.service = service;
        this.lashesService = lashesService;
        this.timeNow = LocalDate.of(2022, 2, 22).atStartOfDay();
        this.collectionSize = service.getAll().size();
        initObject();
    }

    private void initObject() {
        this.customer = new Customer();
        this.customer.setId("testId");
        this.customer.setName("testName");
        this.customer.setSurname("testSurname");

        Lashes lashes = new Lashes();
        lashes.setId("testLashes");
        lashes.setStyle("testStyle");
        customer.setLashesList(new ArrayList<>());
        customer.getLashesList().add(lashes);
        customer.getLashesList().get(0).setDate(timeNow);
        customer.getLashesList().get(0).setNextDate(timeNow.plusMonths(PLUS_MONTHS));
    }

    @AfterEach
    void deleteTestObject() {
        service.delete("testId");
        lashesService.delete("testLashes");
    }

    @Test
    void shouldCreate() {
        //given & when
        service.create(customer);

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());
    }

    @Test
    void shouldUpdate() {
        //given
        Customer customerUpdated = new Customer();
        customerUpdated.setId("testId");
        customerUpdated.setName("testName2");
        customerUpdated.setSurname("testSurname2");

        //when
        service.create(customer);

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //given & when 2
        service.update(customerUpdated);

        //then 2
        assertEquals("testName2", service.getById("testId").getName());
    }

    @Test
    void shouldDelete() {
        //given & when
        service.create(customer);

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //when2
        service.delete("testId");
        assertEquals(collectionSize, service.getAll().size());
    }

    @Test
    void shouldGetAll() {
        //given & when
        service.create(customer);
        List<Customer> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, all.size());
        assertEquals("testName", service.getById("testId").getName());
        assertTrue(all.size() > 0);
        all.stream()
                .filter(it -> it.getLashesList() != null)
                .flatMap(it -> it.getLashesList().stream()
                        .map(lashes -> it.getSurname() + " " + lashes.getNextDate()))
                .forEach(System.out::println);
    }

    @Test
    void shouldGetById() {
        //given & when
        service.create(customer);

        //then
        assertEquals("testName", service.getById("testId").getName());
    }

    @Test
    void shouldGetByNameFragment() {
        //given & when
        service.create(customer);

        //then
        assertEquals("testName", service.getById("testId").getName());
        assertEquals("testSurname", service.getByNameFragment("testName").get(0).getSurname());
    }

    @Test
    void shouldGetLashesWorkNumber() {
        //given & when
        service.create(customer);

        //then
        assertEquals("testName", service.getById("testId").getName());
        assertEquals(1, service.getLashesWorkNumber(customer));
    }

    @Test
    void shouldGetLastWorkDate() {
        //given & when
        service.create(customer);

        //then
        assertEquals("testName", service.getById("testId").getName());
        assertEquals(timeNow, service.getLastWorkDate(customer));
    }

    @Test
    void shouldPartialUpdate() {
        //given
        Map<String, Object> part = new HashMap<>();
        part.put("name", "testName2");

        //when
        service.create(customer);

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //when 2
        service.partialUpdate(customer, part);

        //then 2
        assertEquals("testName2", service.getById("testId").getName());

    }

    @Test
    void shouldGetAllNextWorks() {
        //given & when
        service.create(customer);

        //then
        assertEquals("testName", service.getById("testId").getName());
        assertTrue(service.getAllNextWorks().size() > 0);

        List<CustomerWork> collect = service.getAllNextWorks();
        assertEquals("testName", collect.get(collect.size()-1).getName());
    }

    @Test
    void shouldGetNextWorks() {
        //given & when
        service.create(customer);

        service.getNextWorks("testId", "").stream()
                .map(it -> it.getSurname() + it.getDate())
                .forEach(System.out::println);
        //then
        assertEquals("testName", service.getById("testId").getName());
        assertEquals(1, service.getNextWorks("testId", "").size());
    }

    @Test
    void shouldGetWorksInCalendarMonth() {
        //given & when
        service.create(customer);

        Customer c = new Customer();
        c.setId("testIdC");
        c.setLashesList(new ArrayList<>());
        Lashes l = new Lashes();
        l.setId("lashesIdTestL");
        l.setNextDate(LocalDate.of(2022, 2, 22).plusMonths(PLUS_MONTHS + 1).atStartOfDay());
        c.getLashesList().add(l);
        service.create(c);

        Customer c2 = new Customer();
        c2.setId("testIdC2");
        c2.setLashesList(new ArrayList<>());
        Lashes l2 = new Lashes();
        l2.setId("lashesIdTestL2");
        //28.02 should be visible on March and February sheet
        l2.setNextDate(LocalDate.of(2022, 2, 28).plusMonths(PLUS_MONTHS).atStartOfDay());
        c2.getLashesList().add(l2);
        service.create(c2);

        //then
        assertEquals("testName", service.getById("testId").getName());
        assertEquals(2,
                service.getWorksInCalendarMonth(timeNow.plusMonths(PLUS_MONTHS).toLocalDate())
                        .size());

        assertEquals(2,
                service.getWorksInCalendarMonth(timeNow.plusMonths(PLUS_MONTHS + 1).toLocalDate())
                        .size());

        //cleanUp
        service.delete("testIdC");
        service.delete("testIdC2");
        lashesService.delete("lashesIdTestL");
        lashesService.delete("lashesIdTestL2");
    }

    @Test
    void shouldGetWorksInTheDay() {
        //given & when
        service.create(customer);

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());
        assertEquals(1, service.getWorksInTheDay(timeNow.plusMonths(PLUS_MONTHS).toLocalDate()).size());
    }
}