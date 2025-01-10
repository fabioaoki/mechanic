package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.ProviderPerson;
import br.com.mechanic.mechanic.service.model.ProviderPersonModel;
import br.com.mechanic.mechanic.service.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.service.response.ProviderPersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderPersonMapper {

    ProviderPersonMapper MAPPER = Mappers.getMapper(ProviderPersonMapper.class);

    ProviderPerson toEntity(ProviderPersonRequest dto);

    ProviderPersonResponseDto toDto(ProviderPerson save);

    ProviderPersonModel toModel(ProviderPerson save);

    ProviderPerson modelToEntity(ProviderPersonModel save);
}
