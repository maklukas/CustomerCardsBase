package com.customercard.customercard.service;

import com.customercard.customercard.model.Contact;
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
class ContactServiceTest {

    private final ContactService service;
    private Contact contact;
    private final int collectionSize;

    @Autowired
    public ContactServiceTest(ContactService service) {
        this.service = service;
        this.collectionSize = service.getAll().size();
        initObject();
    }

    private void initObject() {
        this.contact = new Contact(
                "testPhone",
                "testEmail",
                "testStreet",
                "testCity",
                "testBox"
        );
        this.contact.setId("testId");
    }

    @BeforeAll
    void createObject() {
        service.create(contact);
    }

    @AfterAll
    void deleteObject() {
        service.delete("testId");
    }

    @Test
    void shouldGetAll() {
        //given & when
        List<Contact> all = service.getAll();

        //then
        assertEquals(collectionSize + 1, all.size());
        assertTrue(all.size() > 0);
        assertEquals("testStreet", service.getById("testId").getStreet());
        all.stream()
                .map(Contact::getStreet)
                .forEach(System.out::println);
    }

    @Test
    void shouldGetById() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testStreet", service.getById("testId").getStreet());
    }

    @Test
    void shouldGetByName() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testId", service.getByName("testStreet").get(0).getId());
    }

    @Test
    void shouldCreate() {
        assertEquals(collectionSize + 1, service.getAll().size());
    }

    @Test
    void shouldUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testStreet", service.getById("testId").getStreet());

        //given & when 2
        Contact c = new Contact(
                "testPhone",
                "testEmail",
                "testStreet2",
                "testCity",
                "testBox"
        );
        c.setId("testId");
        service.update(c);

        //then 2
        assertEquals("testStreet2", service.getById("testId").getStreet());

        //cleanUp
        c.setStreet("testStreet");
        service.update(c);
    }

    @Test
    @Order(Integer.MAX_VALUE)
    void shouldDelete() {
        //then
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testStreet", service.getById("testId").getStreet());

        //when2
        service.delete("testId");
        assertEquals(collectionSize, service.getAll().size());
    }

    @Test
    void shouldPartialUpdate() {
        //given
        assertEquals(collectionSize + 1, service.getAll().size());
        assertEquals("testStreet", service.getById("testId").getStreet());

        //given2
        Map<String, Object> partial = new HashMap<>();
        partial.put("street", "testStreet2");

        //then
        service.partialUpdate(contact, partial);

        //given
        assertEquals("testStreet2", service.getById("testId").getStreet());

        //cleanUp
        partial.put("street", "testStreet");
        service.partialUpdate(contact, partial);
    }
}