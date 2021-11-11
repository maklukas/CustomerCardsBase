package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Document("contacts")
@NoArgsConstructor
@Getter
@Setter
public class Contact extends AbstractEntity {

    private String phoneNumber;
    @Email
    private String emailAddress;
    private String street;
    private String city;
    private String boxOffice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(phoneNumber, contact.phoneNumber) && Objects.equals(emailAddress, contact.emailAddress) && Objects.equals(street, contact.street) && Objects.equals(city, contact.city) && Objects.equals(boxOffice, contact.boxOffice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, emailAddress, street, city, boxOffice);
    }
}
