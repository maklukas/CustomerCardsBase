package com.customercard.customercard.service;

import com.customercard.customercard.model.Color;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ColorServiceTest {

    private final ColorService service;
    private final Color color;
    private final int collectionSize;

    @Autowired
    public ColorServiceTest(ColorService service) {
        this.service = service;
        this.collectionSize = service.getAll().size();
        this.color = new Color("testName");
        this.color.setId("testId");
    }

    @BeforeAll
    void createObject() {
        service.create(color);
    }

    @AfterAll
    void cleanUp() {
        service.delete("testId");
    }

    @Test
    void shouldGetAll() {
        //given & when
        List<Color> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, all.size());
        assertTrue(all.size() > 0);
        assertEquals("testName", service.getById("testId").getName());
        all.stream()
                .map(Color::getName)
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

        //given
        Color nullNamedColor = new Color();
        nullNamedColor.setId("testNullNamedId");
        assertNull(service.create(nullNamedColor));

        //then
        assertEquals(collectionSize + 1, service.getAll().size());
    }

    @Test
    void shouldUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //given & when 2
        Color c = new Color();
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
    void shouldPartialUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testName", service.getById("testId").getName());

        //given2
        Map<String, Object> partial = new HashMap<>();
        partial.put("name", "testName2");

        //then
        service.partialUpdate(color, partial);

        //given
        assertEquals("testName2", service.getById("testId").getName());

        //cleanUp
        partial.put("name", "testName");
        service.partialUpdate(color, partial);
    }

    @Test
    void shouldFindFirstByName() {
        //given
        Color testName = service.findFirstByName("testName").orElseThrow();

        //then
        assertEquals("testId", testName.getId());
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
}