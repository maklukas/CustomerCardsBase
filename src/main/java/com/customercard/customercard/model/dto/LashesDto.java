package com.customercard.customercard.model.dto;

import com.customercard.customercard.model.Color;
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
    private Color color;
    private String comment;
    private LocalDate date;
    private LocalDate nextDate;
}
