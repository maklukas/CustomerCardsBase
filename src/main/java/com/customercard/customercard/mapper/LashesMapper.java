package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.LashesDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LashesMapper {

    private final ModelMapper mapper;

    @Autowired
    public LashesMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Lashes mapDtoToModel(LashesDto lash) {
        return mapper.map(lash, Lashes.class);
    }

    public LashesDto mapModelToDto(Lashes lash) {
        return mapper.map(lash, LashesDto.class);
    }

    public List<Lashes> mapDtoListToModelList(List<LashesDto> lashes) {
        return lashes.stream()
                .map(lash -> mapper.map(lash, Lashes.class))
                .collect(Collectors.toList());
    }

    public List<LashesDto> mapModelListToDtoList(List<Lashes> lashes) {
        return lashes.stream()
                .map(lash -> mapper.map(lash, LashesDto.class))
                .collect(Collectors.toList());
    }
}
