package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import br.com.mechanic.mechanic.entity.provider.ProviderPerson;
import br.com.mechanic.mechanic.model.ClientPersonModel;
import br.com.mechanic.mechanic.model.ProviderPersonModel;
import br.com.mechanic.mechanic.request.ClientPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.response.ClientPersonResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPersonMapper {

    ClientPersonMapper MAPPER = Mappers.getMapper(ClientPersonMapper.class);

    ClientPerson toEntity(ClientPersonRequest dto);

    ClientPersonResponseDto toDto(ClientPerson save);

    ClientPersonModel toModel(ClientPerson save);

    ClientPerson modelToEntity(ClientPersonModel save);
}
