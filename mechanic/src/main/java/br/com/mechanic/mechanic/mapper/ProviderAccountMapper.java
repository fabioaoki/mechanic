package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderAccount;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderAccountMapper {

    ProviderAccountMapper MAPPER = Mappers.getMapper(ProviderAccountMapper.class);

//    ProviderAccount toEntity(ProviderAccountRequestDto dto);

    @Mapping(target = "lastUpdate", ignore = true)
    ProviderAccountResponseDto toDto(ProviderAccount save);

    @Named("toEntity")
    default ProviderAccount toEntity(ProviderAccountRequestDto dto) {
        return ProviderAccount.builder()
                .workshop(dto.getWorkshop())
                .cnpj(dto.getCnpj().replaceAll("\\s", ""))
                .type(dto.getType())
                .build();
    }
}