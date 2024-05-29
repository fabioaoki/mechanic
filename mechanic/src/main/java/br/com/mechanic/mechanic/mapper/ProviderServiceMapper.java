package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderService;
import br.com.mechanic.mechanic.model.ProviderServiceModel;
import br.com.mechanic.mechanic.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.response.ProviderServiceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProviderServiceMapper {

    ProviderServiceMapper MAPPER = Mappers.getMapper(ProviderServiceMapper.class);

    ProviderService toEntity(ProviderServiceRequest dto);

    ProviderServiceResponseDto toDto(ProviderService entity);

    ProviderServiceModel toModel(ProviderService entity);

    ProviderService modelToEntity(ProviderServiceModel providerServiceModel);
}
