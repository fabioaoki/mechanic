package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.TypeService;
import br.com.mechanic.mechanic.model.TypeServiceModel;
import br.com.mechanic.mechanic.request.TypeServiceRequestDto;
import br.com.mechanic.mechanic.response.TypeServiceResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeServiceMapper {

    TypeServiceMapper MAPPER = Mappers.getMapper(TypeServiceMapper.class);

    TypeService toEntity(TypeServiceRequestDto dto);

    TypeServiceResponseDto toDto(TypeService entity);

    TypeServiceModel toModel(TypeService entity);

    TypeService modelToEntity(TypeServiceModel typeServiceModel);
}
