package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientAddress;
import br.com.mechanic.mechanic.enuns.StateEnum;
import br.com.mechanic.mechanic.model.ClientAddressModel;
import br.com.mechanic.mechanic.request.ClientAddressRequest;
import br.com.mechanic.mechanic.response.ClientAddressResponseByControllerDto;
import br.com.mechanic.mechanic.response.ClientAddressResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientAddressMapper {

    ClientAddressMapper MAPPER = Mappers.getMapper(ClientAddressMapper.class);

    ClientAddressModel requestToModel(ClientAddressRequest addressRequest);

//    @Mapping(target = "createDate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "lastUpdate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "providerAccountId", ignore = true)  // Ignorando campos que não estão no DTO
//    ClientAddress toEntity(ClientAddressRequest dto);

    ClientAddressResponseDto toDto(ClientAddress entity);
    ClientAddressResponseByControllerDto controllerToDto(ClientAddress entity);

    @Named("toEntity")
    default ClientAddress toEntity(ClientAddressRequest dto) {
        double latitude = -23.550520;
        double longitude = -46.633308;
        return ClientAddress.builder()
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
    ClientAddressModel toModel(ClientAddress address);

    @Mapping(target = "state", source = "state", qualifiedByName = "stateEnumToString")
    ClientAddress modelToEntity(ClientAddressModel addressModel);

    @Named("stringToStateEnum")
    default StateEnum stringToStateEnum(String state) {
        return StateEnum.fromString(state);
    }

    @Named("stateEnumToString")
    default String stateEnumToString(StateEnum stateEnum) {
        return stateEnum.toString();
    }

}