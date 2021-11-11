package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Document("styles")
@Getter
@Setter
@NoArgsConstructor
public class Style extends AbstractEntity implements Dictionary {

    @Indexed(unique = true)
    private String name;

    public Style(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Style style = (Style) o;
        return Objects.equals(name, style.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
