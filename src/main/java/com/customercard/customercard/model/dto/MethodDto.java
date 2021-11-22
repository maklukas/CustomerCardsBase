package com.customercard.customercard.model.dto;

import com.customercard.customercard.model.Dictionary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class MethodDto extends Dictionary {
    public MethodDto() {
    }

    public MethodDto(String name) {
        super(name);
    }
}
