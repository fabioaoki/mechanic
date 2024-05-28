package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.ProviderAccount;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.model.ProviderAccountModel;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProviderAccountMapper {

    ProviderAccountMapper MAPPER = Mappers.getMapper(ProviderAccountMapper.class);

    ProviderAccount modelToEntity(ProviderAccountModel model);

    @Named("toEntity")
    default ProviderAccount toEntity(ProviderAccountRequestDto dto) {
        return ProviderAccount.builder()
                .workshop(dto.getWorkshop())
                .cnpj(dto.getCnpj().replaceAll("\\D", ""))
                .type(dto.getType())
                .status(ProviderAccountStatusEnum.INITIAL_BLOCK)
                .build();
    }

    @Named("toDto")
    default ProviderAccountResponseDto toDto(ProviderAccount save) {
        return ProviderAccountResponseDto.builder()
                .id(save.getId())
                .workshop(save.getWorkshop())
                .cnpj((save.getCnpj().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5")))
                .type(save.getType())
                .status(save.getStatus())
                .createDate(save.getCreateDate())
                .lastUpdate(save.getLastUpdate())
                .build();
    }

    ProviderAccountModel toModel(ProviderAccount provider);
}