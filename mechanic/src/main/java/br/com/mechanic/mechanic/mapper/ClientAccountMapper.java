package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.client.ClientAccount;
import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import br.com.mechanic.mechanic.model.ClientAccountModel;
import br.com.mechanic.mechanic.request.ClientAccountRequest;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.ClientAccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientAccountMapper {

    ClientAccountMapper MAPPER = Mappers.getMapper(ClientAccountMapper.class);

    EmployeeAccount toEntity(EmployeeAccountRequest dto);

    @Named("toDtoPage")
   default ClientAccountResponseDto toDto(ClientAccount entity){
        return ClientAccountResponseDto.builder()
                .id(entity.getId())
                .rg(entity.getRg().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{1})", "$1.$2.$3-$4"))
                .cpf(entity.getCpf().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"))
                .lastUpdate(entity.getLastUpdate())
                .createDate(entity.getCreateDate())
                .build();
    }

    ClientAccountModel toModel(ClientAccount entity);

    ClientAccountModel toModel(ClientAccountRequest dto);

    ClientAccount modelToEntity(ClientAccountModel entity);
}