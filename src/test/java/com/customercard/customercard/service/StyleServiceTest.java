package com.customercard.customercard.service;

import static org.junit.jupiter.api.Assertions.*;

import com.customercard.customercard.exception.ItemNotFoundException;
import com.customercard.customercard.model.Style;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class StyleServiceTest {

    private final StyleService service;
    private Style style;
    private final int collectionSize;

    @Autowired
    public StyleServiceTest(StyleService service) {
        this.service = service;
        this.collectionSize = service.getAll().size();
        initObject();
    }

    private void initObject() {
        this.style = new Style();
        this.style.setId("testId");
        this.style.setName("testName");
    }

    @BeforeEach
    void createObject() {
        service.create(style);
    }

    @AfterEach
    void cleanUp() {
        service.delete("testId");
    }

    @Test
    void shouldGetAll() {
        //given & when
        List<Style> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, all.size());
        assertTrue(all.size() > 0);
        assertEquals("testName", service.getById("testId").getName());
        all.stream()
                .map(Style::getName)
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
        Style c = new Style();
        c.setId("testId");
        c.setName("testName2");
        service.update(c);

        //then 2
        assertEquals("testName2", service.getById("testId").getName());
    }

    @Test
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
        service.partialUpdate(style, partial);

        //given
        assertEquals("testName2", service.getById("testId").getName());
    }

    @Test
    void shouldValidateIfExists() {
        //then
        assertTrue(service.validateIfExists(style));
        assertFalse(service.validateIfExists(new Style("XYZTEST")));
    }

    @Test
    void shouldFindFirstByName() {
        //then
        assertThrows(ItemNotFoundException.class, () -> service.findFirstByName("teteteteteteetetetetetest"));
        assertEquals("testName", service.findFirstByName("testName").getName());
    }
}