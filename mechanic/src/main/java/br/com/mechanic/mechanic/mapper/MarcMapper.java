package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.Marc;
import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.model.MarcModel;
import br.com.mechanic.mechanic.model.PlateModel;
import br.com.mechanic.mechanic.request.MarcRequest;
import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.response.MarcResponseDto;
import br.com.mechanic.mechanic.response.PlateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarcMapper {

    MarcMapper MAPPER = Mappers.getMapper(MarcMapper.class);

    Marc toEntity(MarcRequest dto);
    MarcModel dtoToModel(MarcRequest dto);

    MarcResponseDto toDto(Marc entity);

    MarcModel toModel(Marc entity);

    Marc modelToEntity(MarcModel plateModel);
}
