package com.customercard.customercard.mapper;

import com.customercard.customercard.model.Method;
import com.customercard.customercard.model.dto.MethodDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MethodMapper {

    private final ModelMapper mapper;

    @Autowired
    public MethodMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Method mapDtoToModel(MethodDto method) {
        return mapper.map(method, Method.class);
    }

    public MethodDto mapModelToDto(Method method) {
        return mapper.map(method, MethodDto.class);
    }

    public List<Method> mapDtoListToModelList(List<MethodDto> methods) {
        return methods.stream()
                .map(method -> mapper.map(method, Method.class))
                .collect(Collectors.toList());
    }

    public List<MethodDto> mapModelListToDtoList(List<Method> methods) {
        return methods.stream()
                .map(method -> mapper.map(method, MethodDto.class))
                .collect(Collectors.toList());
    }
}
