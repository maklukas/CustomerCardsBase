package com.customercard.customercard.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LashesDto {
    private String id;
    private StyleDto style;
    private MethodDto method;
    private ColorDto color;
    private String comment;
    private LocalDate date;
    private LocalDate nextDate;
}
