package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.ProviderService;
import br.com.mechanic.mechanic.service.model.ProviderServiceModel;
import br.com.mechanic.mechanic.service.model.ProviderServiceModelToUpdate;
import br.com.mechanic.mechanic.service.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.service.response.ProviderServiceIdentifierResponseDto;
import br.com.mechanic.mechanic.service.response.ProviderServiceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderServiceMapper {

    ProviderServiceMapper MAPPER = Mappers.getMapper(ProviderServiceMapper.class);

    ProviderService toEntity(ProviderServiceRequest dto);

//    ProviderServiceResponseDto toDto(ProviderService entity);

    ProviderServiceModel toModel(ProviderService entity);

    ProviderServiceModelToUpdate toModelToUpdate(ProviderService entity);

    ProviderServiceModel dtoToModel(ProviderServiceRequest dto);

    ProviderService modelToEntity(ProviderServiceModelToUpdate modelToUpdate);


    @Named("toDto")
    default ProviderServiceResponseDto toDto(ProviderService entity, ProviderServiceIdentifierResponseDto identifiersList) {
        return ProviderServiceResponseDto.builder()
                .id(entity.getId())
                .providerAccountId(entity.getProviderAccountId())
                .vehicleTypeId(entity.getVehicleTypeId())
                .service(identifiersList)
                .createDate(entity.getCreateDate())
                .lastUpdate(entity.getLastUpdate())
                .isEnabled(entity.getIsEnabled())
                .build();
    }
    @Named("prepareToSave")
    default ProviderService prepareToSave(Long providerAccountId, Long vehicle, Long identifier){
        return ProviderService.builder()
                .providerAccountId(providerAccountId)
                .vehicleTypeId(vehicle)
                .identifierId(identifier)
                .build();
    };
}
