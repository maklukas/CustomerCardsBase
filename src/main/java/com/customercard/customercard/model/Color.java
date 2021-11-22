package com.customercard.customercard.model;


import org.springframework.data.mongodb.core.mapping.Document;

@Document("colors")
public class Color extends Dictionary {

    public Color(String name) {
        super(name);
    }

    public Color() {
    }
}
