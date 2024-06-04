package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.EmployeeAccount;
import br.com.mechanic.mechanic.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeAccountMapper {

    EmployeeAccountMapper MAPPER = Mappers.getMapper(EmployeeAccountMapper.class);

    EmployeeAccount toEntity(EmployeeAccountRequest dto);

    EmployeeAccountResponseDto toDtoPage(EmployeeAccount entity);
    @Named("toDto")
    default EmployeeAccountResponseDto toDto(EmployeeAccount entity, ProviderAccountResponseDto accountResponseDto) {
        return EmployeeAccountResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .role(entity.getRole())
                .birthDate(entity.getBirthDate())
                .providerAccountId(accountResponseDto)
                .lastUpdate(entity.getLastUpdate())
                .build();
    }

    EmployeeAccountModel toModel(EmployeeAccount entity);

    EmployeeAccount modelToEntity(EmployeeAccountModel entity);
}
