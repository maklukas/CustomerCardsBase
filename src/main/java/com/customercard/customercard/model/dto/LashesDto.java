package com.customercard.customercard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LashesDto {
    private String id;
    private String style;
    private String method;
    private String color;
    private String comment;
    private LocalDateTime date;
    private LocalDateTime nextDate;
}
