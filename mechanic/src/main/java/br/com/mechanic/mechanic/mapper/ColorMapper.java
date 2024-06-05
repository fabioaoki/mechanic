package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.Color;
import br.com.mechanic.mechanic.model.ColorModel;
import br.com.mechanic.mechanic.request.ColorRequest;
import br.com.mechanic.mechanic.response.ColorResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ColorMapper {

    ColorMapper MAPPER = Mappers.getMapper(ColorMapper.class);

    @Named("toEntity")
    default Color toEntity(String color){
        return Color.builder().color(color).build();
    }

    ColorResponseDto toDto(Color entity);

    ColorModel toModel(ColorRequest dto);

}