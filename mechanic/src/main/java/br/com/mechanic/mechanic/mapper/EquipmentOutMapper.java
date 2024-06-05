package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import br.com.mechanic.mechanic.model.EquipmentOutModel;
import br.com.mechanic.mechanic.request.EquipmentOutRequest;
import br.com.mechanic.mechanic.response.EquipmentOutResponseDto;
import br.com.mechanic.mechanic.response.EquipmentOutResponseDtoPage;
import br.com.mechanic.mechanic.response.EquipmentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.RoundingMode;

@Mapper
public interface EquipmentOutMapper {

    EquipmentOutMapper MAPPER = Mappers.getMapper(EquipmentOutMapper.class);

    EquipmentOut toEntity(EquipmentOutRequest dto);

    @Named("toDto")
   default EquipmentOutResponseDto toDto(EquipmentOut entity, EquipmentResponseDto equipmentResponseDto){
        return EquipmentOutResponseDto.builder()
                .equipmentId(equipmentResponseDto)
                .amount(entity.getAmount())
                .createDate(entity.getCreateDate())
                .lastUpdate(entity.getLastUpdate())
                .transactionId(entity.getTransactionId())
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
                .amount(entity.getAmount().setScale(2, RoundingMode.HALF_UP))
                .reversal(entity.getReversal())
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
                .amount(dto.getAmount().setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    EquipmentOutResponseDtoPage toDtoPage(EquipmentOut equipmentOut);
}
