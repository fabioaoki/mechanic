package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderPhoneMapper {

    ProviderPhoneMapper MAPPER = Mappers.getMapper(ProviderPhoneMapper.class);

    ProviderPhone toEntity(ProviderPhoneRequest dto);

    ProviderPhoneRequest toDto(ProviderPhone save);
}
