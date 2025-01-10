package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import br.com.mechanic.mechanic.service.model.ClientPersonModel;
import br.com.mechanic.mechanic.service.request.ClientPersonRequest;
import br.com.mechanic.mechanic.service.response.ClientPersonResponseByControllerDto;
import br.com.mechanic.mechanic.service.response.ClientPersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPersonMapper {

    ClientPersonMapper MAPPER = Mappers.getMapper(ClientPersonMapper.class);

    ClientPerson toEntity(ClientPersonRequest dto);

    ClientPersonResponseDto toDto(ClientPerson save);

    ClientPersonResponseByControllerDto byControllerToDto(ClientPerson save);

    ClientPersonModel toModel(ClientPerson save);

    ClientPerson modelToEntity(ClientPersonModel save);
}
