package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import br.com.mechanic.mechanic.service.model.EquipmentOutModel;
import br.com.mechanic.mechanic.service.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.service.response.EquipmentOutResponseDto;
import br.com.mechanic.mechanic.service.response.EquipmentOutResponseDtoPage;
import br.com.mechanic.mechanic.service.response.EquipmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EquipmentOutMapper {

    EquipmentOutMapper MAPPER = Mappers.getMapper(EquipmentOutMapper.class);

    EquipmentOut toEntity(EquipmentOutRequest dto);

    @Named("toDto")
    default EquipmentOutResponseDto toDto(EquipmentOut entity, EquipmentResponseDto equipmentResponseDto) {
        return EquipmentOutResponseDto.builder()
                .equipment(equipmentResponseDto)
                .createDate(entity.getCreateDate())
                .lastUpdate(entity.getLastUpdate())
                .completedServiceId(entity.getCompletedServiceId())
                .providerAccountId(entity.getProviderAccountId())
                .id(entity.getId())
                .build();
    }

    @Named("requestToModel")
    default EquipmentOutModel toModel(EquipmentOut entity) {
        return EquipmentOutModel.builder()
                .id(entity.getId())
                .providerAccountId(entity.getProviderAccountId())
                .equipmentId(entity.getEquipmentId())
                .reversal(entity.getReversal())
                .completedServiceId(entity.getCompletedServiceId())
                .createDate(entity.getCreateDate())
                .lastUpdate(entity.getLastUpdate())
                .build();
    }

    EquipmentOut modelToEntity(EquipmentOutModel equipmentOutModel);

    @Named("requestToModel")
    default EquipmentOutModel requestToModel(EquipmentOutRequest dto) {
        return EquipmentOutModel.builder()
                .providerAccountId(dto.getProviderAccountId())
                .equipmentId(dto.getEquipmentId())
                .completedServiceId(dto.getCompletedServiceId())
                .completedServiceId(dto.getCompletedServiceId())
                .build();
    }

    EquipmentOutResponseDtoPage toDtoPage(EquipmentOut equipmentOut);

    EquipmentOutResponseDto toDto(EquipmentOut equipmentOut);

    @Named("completedServiceToEquipmentOut")
    default EquipmentOutRequest completedServiceToEquipmentOut(Long providerAccountId, Long completedServiceId, Long equipmentId) {
        return EquipmentOutRequest.builder().equipmentId(equipmentId).completedServiceId(completedServiceId).providerAccountId(providerAccountId).build();
    }
}
