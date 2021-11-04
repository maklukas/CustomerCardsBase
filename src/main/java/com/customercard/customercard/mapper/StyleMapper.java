package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Style;
import com.customercard.customercard.model.dto.StyleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StyleMapper {

    private final ModelMapper mapper;

    @Autowired
    public StyleMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Style mapDtoToModel(StyleDto style) {
        return mapper.map(style, Style.class);
    }

    public StyleDto mapModelToDto(Style style) {
        return mapper.map(style, StyleDto.class);
    }

    public List<Style> mapDtoListToModelList(List<StyleDto> styles) {
        return styles.stream()
                .map(style -> mapper.map(style, Style.class))
                .collect(Collectors.toList());
    }

    public List<StyleDto> mapModelListToDtoList(List<Style> styles) {
        return styles.stream()
                .map(style -> mapper.map(style, StyleDto.class))
                .collect(Collectors.toList());
    }

}
