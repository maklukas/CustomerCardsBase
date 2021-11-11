package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document("customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends AbstractEntity {

    private String name;
    private String surname;
    private String comment;
    private Contact contact;
    private List<Lashes> lashesList;

    public Customer(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.lashesList = new ArrayList<>();
    }
}
