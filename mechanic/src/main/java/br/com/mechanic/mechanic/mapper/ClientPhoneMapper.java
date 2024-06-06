package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import br.com.mechanic.mechanic.entity.provider.ProviderPhone;
import br.com.mechanic.mechanic.model.ClientPhoneModel;
import br.com.mechanic.mechanic.model.ProvidePhoneModel;
import br.com.mechanic.mechanic.request.ClientPhoneRequest;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.response.ClientPhoneResponseDto;
import br.com.mechanic.mechanic.response.ProviderPhoneResponseDto;
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
