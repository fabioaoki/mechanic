package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderAddress;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderAddressMapper {

    ProviderAddressMapper MAPPER = Mappers.getMapper(ProviderAddressMapper.class);

    @Mapping(target = "createDate", ignore = true)  // Ignorando campos que não estão no DTO
    @Mapping(target = "lastUpdate", ignore = true)  // Ignorando campos que não estão no DTO
    @Mapping(target = "providerAccountId", ignore = true)  // Ignorando campos que não estão no DTO
    ProviderAddress toEntity(ProviderAddressRequest dto);

    ProviderAddressRequest toDto(ProviderAddress entity);
}