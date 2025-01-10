package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import br.com.mechanic.mechanic.service.model.ClientPhoneModel;
import br.com.mechanic.mechanic.service.request.ClientPhoneRequest;
import br.com.mechanic.mechanic.service.response.ClientPhoneResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientPhoneMapper {

    ClientPhoneMapper MAPPER = Mappers.getMapper(ClientPhoneMapper.class);

    ClientPhone toEntity(ClientPhoneRequest dto);

    ClientPhoneResponseDto toDto(ClientPhone entity);

    ClientPhoneModel toModel(ClientPhone entity);

    ClientPhone modelToEntity(ClientPhoneModel phoneModel);
}
