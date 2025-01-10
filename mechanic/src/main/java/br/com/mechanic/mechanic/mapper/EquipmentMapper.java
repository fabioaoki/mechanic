package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.Equipment;
import br.com.mechanic.mechanic.service.model.EquipmentModel;
import br.com.mechanic.mechanic.service.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.service.response.EquipmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentMapper {

    EquipmentMapper MAPPER = Mappers.getMapper(EquipmentMapper.class);

    Equipment toEntity(EquipmentRequestDto dto);

    EquipmentResponseDto toDto(Equipment entity);

    EquipmentModel toModel(Equipment entity);

    EquipmentModel requestToModel(EquipmentRequestDto dto);

    Equipment modelToEntity(EquipmentModel typeServiceModel);
}
