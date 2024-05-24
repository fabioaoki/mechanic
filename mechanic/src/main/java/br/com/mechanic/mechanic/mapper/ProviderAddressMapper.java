package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderAccount;
import br.com.mechanic.mechanic.entity.ProviderAddress;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderAddressMapper {

    ProviderAddressMapper MAPPER = Mappers.getMapper(ProviderAddressMapper.class);

//    @Mapping(target = "createDate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "lastUpdate", ignore = true)  // Ignorando campos que não estão no DTO
//    @Mapping(target = "providerAccountId", ignore = true)  // Ignorando campos que não estão no DTO
//    ProviderAddress toEntity(ProviderAddressRequest dto);

    ProviderAddressRequest toDto(ProviderAddress entity);

    @Named("toEntity")
    default ProviderAddress toEntity(ProviderAddressRequest dto) {
        double latitude = -23.550520;
        double longitude = -46.633308;
        return ProviderAddress.builder()
                .city(dto.getCity())
                .zipCode(dto.getZipCode().replaceAll("\\s", ""))
                .street(dto.getStreet())
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}