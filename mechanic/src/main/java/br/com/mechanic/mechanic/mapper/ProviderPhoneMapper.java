package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.ProviderPhone;
import br.com.mechanic.mechanic.service.model.ProvidePhoneModel;
import br.com.mechanic.mechanic.service.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.service.response.ProviderPhoneResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderPhoneMapper {

    ProviderPhoneMapper MAPPER = Mappers.getMapper(ProviderPhoneMapper.class);

    ProviderPhone toEntity(ProviderPhoneRequest dto);

    ProviderPhoneResponseDto toDto(ProviderPhone entity);

    ProvidePhoneModel toModel(ProviderPhone entity);

    ProviderPhone modelToEntity(ProvidePhoneModel phoneModel);
}
