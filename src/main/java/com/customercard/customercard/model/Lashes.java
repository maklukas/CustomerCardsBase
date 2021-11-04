package com.customercard.customercard.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

}
