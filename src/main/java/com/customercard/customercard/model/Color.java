package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Objects.equals(name, color.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
