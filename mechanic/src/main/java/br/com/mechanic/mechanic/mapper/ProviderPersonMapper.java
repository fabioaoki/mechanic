package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import br.com.mechanic.mechanic.model.ProviderPersonResponseModel;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderPersonMapper {

    ProviderPersonMapper MAPPER = Mappers.getMapper(ProviderPersonMapper.class);

    ProviderPerson toEntity(ProviderPersonRequest dto);

    ProviderPersonResponseDto toDto(ProviderPerson save);

    ProviderPersonResponseModel toModel(ProviderPerson save);

    ProviderPerson modelToEntity(ProviderPersonResponseModel save);
}
