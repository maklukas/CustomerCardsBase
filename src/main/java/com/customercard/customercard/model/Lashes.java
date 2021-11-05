package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Document("lashes")
@Setter
@Getter
@NoArgsConstructor
public class Lashes {
    @Id
    @NotNull
    private String id;
    private Style style;
    private Method method;
    private Color color;
    private String comment;
    private LocalDate date;
    private LocalDate nextDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lashes lashes = (Lashes) o;
        return Objects.equals(style, lashes.style) && Objects.equals(method, lashes.method) && Objects.equals(color, lashes.color) && Objects.equals(comment, lashes.comment) && Objects.equals(date, lashes.date) && Objects.equals(nextDate, lashes.nextDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(style, method, color, comment, date, nextDate);
    }
}
