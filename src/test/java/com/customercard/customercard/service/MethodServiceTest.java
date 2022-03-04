package com.customercard.customercard.service;

import static org.junit.jupiter.api.Assertions.*;

import com.customercard.customercard.model.Method;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MethodServiceTest {

    private final MethodService service;
    private final Method method;
    private final int collectionSize;

    @Autowired
    public MethodServiceTest(MethodService service) {
        this.service = service;
        this.collectionSize = service.getAll().size();
        this.method = new Method("testName");
        this.method.setId("testId");
    }

    @BeforeAll
    void createObject() {
        service.create(method);
    }

    @AfterAll
    void cleanUp() {
        service.delete("testId");
    }

    @Test
    void shouldGetAll() {
        //given & when
        List<Method> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, all.size());
        assertTrue(all.size() > 0);
        assertEquals("testName", service.getById("testId").getName());
        all.stream()
                .map(Method::getName)
                .forEach(System.out::println);
    }

    @Test
    void shouldGetById() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());
    }

    @Test
    void shouldGetByName() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testId", service.getByName("testName").get(0).getId());
    }

    @Test
    void shouldCreate() {
        assertEquals(collectionSize + 1, service.getAll().size());
    }

    @Test
    void shouldUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //given & when 2
        Method c = new Method();
        c.setId("testId");
        c.setName("testName2");
        service.update(c);

        //then 2
        assertEquals("testName2", service.getById("testId").getName());

        //cleanUp
        c.setName("testName");
        service.update(c);
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void shouldDelete() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //when2
        service.delete("testId");
        assertEquals(collectionSize, service.getAll().size());
    }

    @Test
    void shouldPartialUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //given2
        Map<String, Object> partial = new HashMap<>();
        partial.put("name", "testName2");

        //then
        service.partialUpdate(method, partial);

        //given
        assertEquals("testName2", service.getById("testId").getName());

        //cleanUp
        partial.put("name", "testName");
        service.partialUpdate(method, partial);
    }

    @Test
    void shouldFindFirstByName() {
        //given
        Method testName = service.findFirstByName("testName").orElseThrow();

        //then
        assertEquals("testId", testName.getId());
    }
}