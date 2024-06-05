package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.ProviderPerson;
import br.com.mechanic.mechanic.model.ProviderPersonModel;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
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
