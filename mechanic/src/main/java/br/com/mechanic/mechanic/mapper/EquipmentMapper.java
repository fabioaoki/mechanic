package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.Equipment;
import br.com.mechanic.mechanic.model.EquipmentModel;
import br.com.mechanic.mechanic.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
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
