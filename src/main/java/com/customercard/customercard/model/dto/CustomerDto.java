package com.customercard.customercard.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {

    private String id;
    private String name;
    private String surname;
    private String comment;
    private ContactDto contact;
    private List<LashesDto> lashesList;

    public CustomerDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.lashesList = new ArrayList<>();
    }
}
