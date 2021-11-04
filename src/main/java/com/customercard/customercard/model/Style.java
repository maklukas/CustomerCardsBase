package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document("styles")
@Getter
@Setter
@NoArgsConstructor
public class Style {
    @Id
    @NotNull
    private String id;
    @Indexed(unique = true)
    private String name;

    public Style(String name) {
        this.name = name;
    }
}
