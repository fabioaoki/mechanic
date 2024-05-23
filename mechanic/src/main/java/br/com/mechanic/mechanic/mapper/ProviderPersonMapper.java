package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderPersonMapper {

    ProviderPersonMapper MAPPER = Mappers.getMapper(ProviderPersonMapper.class);

    ProviderPerson toEntity(ProviderPersonRequest dto);

    ProviderPersonRequest toDto(ProviderPerson save);
}
