package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("colors")
@Getter
@Setter
@NoArgsConstructor
public class Color {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;

    public Color(String name) {
        this.name = name;
    }

}
