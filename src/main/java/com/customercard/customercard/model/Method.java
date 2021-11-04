package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Document("methods")
@Getter
@Setter
@NoArgsConstructor
public class Method {
    @Id
    @NotNull
    private String id;
    @Indexed(unique = true)
    private String name;

    public Method(String name) {
        this.name = name;
    }
}
