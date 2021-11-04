package com.customercard.customercard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGeneralDto {

    private String id;
    private String name;
    private String surname;
    private LocalDate lastDate;
    private int totalWorks;

}
