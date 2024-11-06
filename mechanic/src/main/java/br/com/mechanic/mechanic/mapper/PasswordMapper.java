package br.com.mechanic.mechanic.mapper;

import br.com.mechanic.mechanic.entity.provider.ProviderAccount;
import br.com.mechanic.mechanic.entity.provider.ProviderPassword;
import br.com.mechanic.mechanic.model.PasswordModel;
import br.com.mechanic.mechanic.response.PasswordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper
public interface PasswordMapper {

    PasswordMapper MAPPER = Mappers.getMapper(PasswordMapper.class);

    @Named("toEntity")
    default ProviderPassword toEntity(Long providerAccountId, String password, String login) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        return ProviderPassword.builder()
                .providerAccountId(providerAccountId)
                .password(encodedPassword)
                .login(login)
                .build();
    }

    PasswordResponse toDto(ProviderPassword entity);


    ProviderPassword modelToEntity(PasswordModel phoneModel);

    PasswordModel toModel(ProviderPassword providerPassword);

}
