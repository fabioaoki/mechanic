package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.vehicle.Vehicle;
import br.com.mechanic.mechanic.service.model.VehicleModel;
import br.com.mechanic.mechanic.service.request.SaveVehicleRequest;
import br.com.mechanic.mechanic.service.response.VehicleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VehicleMapper {

    VehicleMapper MAPPER = Mappers.getMapper(VehicleMapper.class);

    Vehicle toEntity(SaveVehicleRequest dto);

    VehicleResponseDto toDto(Vehicle entity);

    VehicleModel toModel(Vehicle entity);

    Vehicle modelToEntity(VehicleModel vehicleTypeModel);
}
