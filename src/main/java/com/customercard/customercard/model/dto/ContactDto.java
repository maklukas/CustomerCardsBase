package com.customercard.customercard.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContactDto {

    private String id;
    private String phoneNumber;
    private String emailAddress;
    private String street;
    private String city;
    private String boxOffice;
}
