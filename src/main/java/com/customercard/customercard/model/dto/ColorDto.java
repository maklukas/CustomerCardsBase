package com.customercard.customercard.model.dto;

import com.customercard.customercard.model.Dictionary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ColorDto implements Dictionary {
    private String id;
    private String name;
}
