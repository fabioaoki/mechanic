package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import br.com.mechanic.mechanic.service.model.VehicleTypeModel;
import br.com.mechanic.mechanic.service.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.service.response.VehicleTypeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VehicleTypeMapper {

    VehicleTypeMapper MAPPER = Mappers.getMapper(VehicleTypeMapper.class);

    VehicleType toEntity(VehicleTypeRequestDto dto);

    VehicleTypeResponseDto toDto(VehicleType entity);

    VehicleTypeModel toModel(VehicleType entity);

    VehicleType modelToEntity(VehicleTypeModel vehicleTypeModel);
}
