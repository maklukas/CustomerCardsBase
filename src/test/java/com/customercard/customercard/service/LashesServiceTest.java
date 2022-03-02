package com.customercard.customercard.service;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LashesServiceTest {

    private final LashesService service;
    private final StyleService styleService;
    private final MethodService methodService;
    private final ColorService colorService;
    private final CustomerService customerService;
    private final Lashes lashes;
    private final int collectionSize;
    private final LocalDateTime testDate;

    @Autowired
    public LashesServiceTest(
            LashesService service,
            StyleService styleService,
            MethodService methodService,
            ColorService colorService,
            CustomerService customerService
    ) {
        this.service = service;
        this.styleService = styleService;
        this.methodService = methodService;
        this.colorService = colorService;
        this.customerService = customerService;
        this.testDate = LocalDate.of(1990, 1, 1).atStartOfDay();
        this.lashes = new Lashes();
        this.collectionSize = service.getAll().size();
        initObject();
    }

    private void initObject() {
        this.lashes.setId("testId");
        this.lashes.setStyle("testStyle");
        this.lashes.setColor("testColor");
        this.lashes.setMethod("testMethod");
        this.lashes.setComment("testComment");
        this.lashes.setDate(testDate);
        this.lashes.setNextDate(testDate.plusMonths(1));
    }

    @BeforeAll
    void createObject() {
        service.create(lashes);
    }

    @AfterAll
    void deleteObject() {
        service.delete("testId");

        styleService.getByName("testStyle")
                .forEach(it -> styleService.delete(it.getId()));

        methodService.getByName("testMethod")
                .forEach(it -> methodService.delete(it.getId()));

        colorService.getByName("testColor")
                .forEach(it -> colorService.delete(it.getId()));
    }

    @Test
    void shouldGetAll() {
        //given & when
        List<Lashes> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertTrue(all.size() > 0);
        assertEquals("testComment", service.getById("testId").getComment());
        all.stream()
                .map(Lashes::getComment)
                .forEach(System.out::println);
    }

    @Test
    void shouldGetById() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testComment", service.getById("testId").getComment());
    }

    @Test
    void shouldGetByComment() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testId", service.getByComment("testComment").get(0).getId());
    }

    @Test
    void shouldCreate() {
        assertEquals(collectionSize + 1, service.getAll().size());
    }

    @Test
    void shouldUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testComment", service.getById("testId").getComment());

        //given & when 2
        Lashes l = new Lashes(
                "testStyle",
                "testMethod",
                "testColor",
                "testComment2",
                testDate,
                testDate.plusMonths(1)
        );
        l.setId("testId");
        service.update(l);

        //then 2
        assertEquals("testComment2", service.getById("testId").getComment());

        //cleanUp
        l.setComment("testComment");
        service.update(l);
    }

    @Test
    void shouldPartialUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testComment", service.getById("testId").getComment());

        //given2
        Map<String, Object> partial = new HashMap<>();
        partial.put("comment", "testComment2");

        //then
        service.partialUpdate(lashes, partial);

        //given
        assertEquals("testComment2", service.getById("testId").getComment());

        //cleanUp
        partial.put("comment", "testComment");
        service.partialUpdate(lashes, partial);
    }

    @Test
    void shouldGetAllForTheCustomer() {
        //given
        Customer customer = new Customer(
                "testName",
                "testSurname"
        );
        customer.setId("testId");
        customer.setLashesList(List.of(lashes));
        customerService.create(customer);

        //when
        List<Lashes> customerLashesWorks = service.getAll(customer, "testMethod");

        //then
        assertEquals(1, customerLashesWorks.size());
        assertEquals("testColor", customerLashesWorks.get(0).getColor());

        //cleanUp
        customerService.delete("testId");
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void shouldDelete() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testComment", service.getById("testId").getComment());

        //when2
        service.delete("testId");
        assertEquals(collectionSize, service.getAll().size());
    }
}