package com.customercard.customercard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWork implements Comparable<CustomerWork> {

    private String id;
    private String name;
    private String surname;
    private LocalDateTime date;

    @Override
    public int compareTo(@NotNull CustomerWork o) {
        if (getDate() == null || o.getDate() == null) return 0;
        return getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        return  formatter.format(date) + " | " + name +  " " + surname;
    }
}
