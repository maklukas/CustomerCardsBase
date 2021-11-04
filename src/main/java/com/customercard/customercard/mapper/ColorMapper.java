package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.dto.ColorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ColorMapper {

    private final ModelMapper mapper;

    @Autowired
    public ColorMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Color mapDtoToModel(ColorDto color) {
        return mapper.map(color, Color.class);
    }

    public ColorDto mapModelToDto(Color color) {
        return mapper.map(color, ColorDto.class);
    }

    public List<Color> mapDtoListToModelList(List<ColorDto> colors) {
        return colors.stream()
                .map(color -> mapper.map(color, Color.class))
                .collect(Collectors.toList());
    }

    public List<ColorDto> mapModelListToDtoList(List<Color> colors) {
        return colors.stream()
                .map(color -> mapper.map(color, ColorDto.class))
                .collect(Collectors.toList());
    }
}
