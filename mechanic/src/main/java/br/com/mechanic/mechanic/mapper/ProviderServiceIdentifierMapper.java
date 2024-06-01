package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderServiceIdentifier;
import br.com.mechanic.mechanic.response.ProviderServiceIdentifierResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderServiceIdentifierMapper {

    ProviderServiceIdentifierMapper MAPPER = Mappers.getMapper(ProviderServiceIdentifierMapper.class);

    ProviderServiceIdentifierResponseDto toDto(ProviderServiceIdentifier entity);
}
