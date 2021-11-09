package com.customercard.customercard.service;

import com.customercard.customercard.model.Contact;
import com.customercard.customercard.repository.ContactRepo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("contactService")
public class ContactService {

    private final ContactRepo repo;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    public ContactService(ContactRepo repo) {
        this.repo = repo;
    }

    public List<Contact> getAllContacts() {
        LOGGER.info("All contacts fetched.");
        return repo.findAll();
    }

    public Contact getContactById(@Nullable String id) {
        LOGGER.info("Contact fetched by id.");
        assert id != null;
        return repo.findById(id).orElse(new Contact());
    }

    public List<Contact> getContactByName(@Nullable String txt) {
        LOGGER.info("All fetched by name.");
        return repo.findAll()
                .stream()
                .filter(contact -> StringUtils.containsIgnoreCase(contact.getCity(), txt)
                        || StringUtils.containsIgnoreCase(contact.getBoxOffice(), txt)
                        || StringUtils.containsIgnoreCase(contact.getEmailAddress(), txt)
                        || StringUtils.containsIgnoreCase(contact.getStreet(), txt)
                        || StringUtils.containsIgnoreCase(contact.getPhoneNumber(), txt))
                .collect(Collectors.toList());
    }

    public List<Contact> getContacts(@Nullable String id, @Nullable String txt) {
        if (id != null) {
            return List.of(getContactById(id));
        } else if (txt != null) {
            return getContactByName(txt);
        } else {
            return getAllContacts();
        }
    }

    public Contact createContact(Contact contact) {

        if (validateIfExists(contact)) {
            return findFirstEqual(contact);
        }

        LOGGER.info("Contact created.");
        return repo.save(contact);
    }

    public Contact updateContact(Contact contact) {

        if (validateIfExists(contact)) {
            return findFirstEqual(contact);
        }

        LOGGER.info("Contact updated.");
        return repo.save(contact);
    }

    public boolean deleteContact(String id) {
        LOGGER.info("Contact deleted.");
        repo.deleteById(id);
        return true;
    }

    public boolean partialUpdate(Contact contact, Map<String, Object> updates) {
        LOGGER.info("Contact particular updated.");
        if (updates.containsKey("phoneNumber")) {
            contact.setPhoneNumber((String) updates.get("phoneNumber"));
        }
        if (updates.containsKey("emailAddress")) {
            contact.setEmailAddress((String) updates.get("emailAddress"));
        }
        if (updates.containsKey("street")) {
            contact.setStreet((String) updates.get("street"));
        }
        if (updates.containsKey("city")) {
            contact.setCity((String) updates.get("city"));
        }
        if (updates.containsKey("boxOffice")) {
            contact.setBoxOffice((String) updates.get("boxOffice"));
        }
        updateContact(contact);
        return true;
    }

    public boolean validateIfExists(Contact contact) {

        if (findFirstEqual(contact) != null) {
            LOGGER.info("Contact already exists.");
            return true;
        }
        return false;
    }

    public Contact findFirstEqual(Contact contact) {
        if (repo.findAll().size() > 0) {
            return repo.findAll().stream()
                    .filter(c -> c.equals(contact))
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

}
