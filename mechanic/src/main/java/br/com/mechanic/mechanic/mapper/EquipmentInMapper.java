package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import br.com.mechanic.mechanic.service.model.EquipmentInModel;
import br.com.mechanic.mechanic.service.request.EquipmentInRequest;
import br.com.mechanic.mechanic.service.request.EquipmentInUpdateRequest;
import br.com.mechanic.mechanic.service.request.EquipmentRequestDto;
import br.com.mechanic.mechanic.service.response.EquipmentInResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.RoundingMode;

@Mapper
public interface EquipmentInMapper {

    EquipmentInMapper MAPPER = Mappers.getMapper(EquipmentInMapper.class);

    EquipmentIn toEntity(EquipmentRequestDto dto);

    EquipmentInResponseDto toDto(EquipmentIn entity);

    @Named("requestToModel")
    default EquipmentInModel toModel(EquipmentIn entity) {
        return EquipmentInModel.builder()
                .id(entity.getId())
                .providerAccountId(entity.getProviderAccountId())
                .equipmentId(entity.getEquipmentId())
                .quantity(entity.getQuantity())
                .amount(entity.getAmount().setScale(2, RoundingMode.HALF_UP))
                .finish(entity.isFinish())
                .createDate(entity.getCreateDate())
                .lastUpdate(entity.getLastUpdate())
                .build();
    }

    EquipmentIn modelToEntity(EquipmentInModel typeServiceModel);

    @Named("requestToModel")
    default EquipmentInModel requestToModel(EquipmentInRequest dto) {
        return EquipmentInModel.builder()
                .providerAccountId(dto.getProviderAccountId())
                .equipmentId(dto.getEquipmentId())
                .quantity(dto.getQuantity())
                .amount(dto.getAmount().setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    EquipmentInUpdateRequest mapperUpdate(EquipmentInResponseDto equipmentInResponse);
}
