package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Document("contacts")
@NoArgsConstructor
@Getter
@Setter
public class Contact {

    @Id
    @NotNull
    private String id;
    private String phoneNumber;
    @Email
    private String emailAddress;
    private String street;
    private String city;
    private String boxOffice;
}
