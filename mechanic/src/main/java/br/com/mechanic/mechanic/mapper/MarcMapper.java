package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.Model;
import br.com.mechanic.mechanic.model.MarcModel;
import br.com.mechanic.mechanic.request.MarcRequest;
import br.com.mechanic.mechanic.response.MarcResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarcMapper {

    MarcMapper MAPPER = Mappers.getMapper(MarcMapper.class);

    Model toEntity(MarcRequest dto);
    MarcModel dtoToModel(MarcRequest dto);

    MarcResponseDto toDto(Model entity);

    MarcModel toModel(Model entity);

    Model modelToEntity(MarcModel plateModel);
}
