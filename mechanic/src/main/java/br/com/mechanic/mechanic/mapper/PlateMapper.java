package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.model.PlateModel;
import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.response.PlateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlateMapper {

    PlateMapper MAPPER = Mappers.getMapper(PlateMapper.class);

    Plate toEntity(PlateRequest dto);
    PlateModel dtoToModel(PlateRequest dto);

    PlateResponseDto toDto(Plate entity);

    PlateModel toModel(Plate entity);

    Plate modelToEntity(PlateModel plateModel);
}
