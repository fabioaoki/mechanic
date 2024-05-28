package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import br.com.mechanic.mechanic.enuns.StateEnum;
import br.com.mechanic.mechanic.model.ProviderAddressModel;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.response.ProviderAddressResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderAddressMapper {

    ProviderAddressMapper MAPPER = Mappers.getMapper(ProviderAddressMapper.class);

    ProviderAddressModel requestToModel(ProviderAddressRequest addressRequest);

//    @Mapping(target = "createDate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "lastUpdate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "providerAccountId", ignore = true)  // Ignorando campos que não estão no DTO
//    ProviderAddress toEntity(ProviderAddressRequest dto);

    ProviderAddressResponseDto toDto(ProviderAddress entity);

    @Named("toEntity")
    default ProviderAddress toEntity(ProviderAddressRequest dto) {
        double latitude = -23.550520;
        double longitude = -46.633308;
        return ProviderAddress.builder()
                .city(dto.getCity())
                .zipCode(dto.getZipCode().replaceAll("\\s", ""))
                .street(dto.getStreet())
                .state(dto.getState().toString())
                .neighborhood(dto.getNeighborhood())
                .number(dto.getNumber())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    @Mapping(target = "state", source = "state", qualifiedByName = "stringToStateEnum")
    ProviderAddressModel toModel(ProviderAddress address);

    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    ProviderAddress modelToEntity(ProviderAddressModel addressModel);

    @Named("stringToStateEnum")
    default StateEnum stringToStateEnum(String state) {
        return StateEnum.fromString(state);
    }

    @Named("stateEnumToString")
    default String stateEnumToString(StateEnum stateEnum) {
        return stateEnum.toString();
    }

}