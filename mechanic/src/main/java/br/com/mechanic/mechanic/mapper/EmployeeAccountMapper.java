package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import br.com.mechanic.mechanic.service.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.service.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.service.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.service.response.EmployeeAccountResponseDtoPage;
import br.com.mechanic.mechanic.service.response.ProviderAccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeAccountMapper {

    EmployeeAccountMapper MAPPER = Mappers.getMapper(EmployeeAccountMapper.class);

    EmployeeAccount toEntity(EmployeeAccountRequest dto);

    @Named("toDtoPage")
   default EmployeeAccountResponseDtoPage toDtoPage(EmployeeAccount entity){
        return EmployeeAccountResponseDtoPage.builder()
                .id(entity.getId())
                .name(entity.getName())
                .role(entity.getRole())
                .birthDate(entity.getBirthDate())
                .providerAccountId(entity.getProviderAccountId())
                .lastUpdate(entity.getLastUpdate())
                .createDate(entity.getCreateDate())
                .cpf(entity.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"))
                .build();
    }

    @Named("toDto")
    default EmployeeAccountResponseDto toDto(EmployeeAccount entity, ProviderAccountResponseDto accountResponseDto) {
        return EmployeeAccountResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .role(entity.getRole())
                .birthDate(entity.getBirthDate())
                .providerAccountId(accountResponseDto)
                .lastUpdate(entity.getLastUpdate())
                .createDate(entity.getCreateDate())
                .cpf(entity.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"))
                .build();
    }

    EmployeeAccountModel toModel(EmployeeAccount entity);

    EmployeeAccountModel toModel(EmployeeAccountRequest dto);

    EmployeeAccount modelToEntity(EmployeeAccountModel entity);
}