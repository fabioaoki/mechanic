package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.ProviderPassword;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.PasswordException;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.mapper.PasswordMapper;
import br.com.mechanic.mechanic.model.PasswordModel;
import br.com.mechanic.mechanic.repository.provider.ProviderPasswordRepositoryImpl;
import br.com.mechanic.mechanic.request.PasswordRequestDto;
import br.com.mechanic.mechanic.response.PasswordResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Log4j2
@Service
public class PasswordService implements PasswordServiceBO {

    private final ProviderPasswordRepositoryImpl passwordRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void save(Long providerAccountId, String password, String email) {
        log.info("Service: valid password");
        validPassword(password);
        log.info("Service: Saving a password");
        ProviderPassword providerPassword = PasswordMapper.MAPPER.toEntity(providerAccountId, password, email);
        passwordRepository.save(providerPassword);
    }

    @Override
    public boolean matches(Long providerAccountId, String password) {
        PasswordModel passwordModel = PasswordMapper.MAPPER.toModel(getProviderPassword(providerAccountId));
        return encoder.matches(password, passwordModel.getPassword());
    }

    @Override
    public PasswordResponse findById(Long id) {
        return PasswordMapper.MAPPER.toDto(getProviderPassword(id));
    }

    @Override
    public PasswordResponse findByProviderAccountId(Long providerAccountId) {
        return PasswordMapper.MAPPER.toDto(getProviderPassword(providerAccountId));
    }


    @Override
    public void updateProviderPassword(Long providerAccountId, PasswordRequestDto passwordRequestDto) {
        log.info("Service update password by providerAccountId: {}", providerAccountId);
        validPassword(passwordRequestDto.getPassword());
        PasswordModel passwordModel = PasswordMapper.MAPPER.toModel(getProviderPassword(providerAccountId));
        boolean isChange = updateField(passwordModel, passwordRequestDto);
        if (isChange) {
            log.info("Service: Change password");
            passwordRepository.save(PasswordMapper.MAPPER.modelToEntity(passwordModel));
        } else {
            throw new PasswordException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider password.");
        }
    }

    private boolean updateField(PasswordModel passwordModel, PasswordRequestDto PasswordRequestDto) {
        log.info("Service: Starting password update process");
        boolean isChange = false;

        if (!encoder.matches(PasswordRequestDto.getPassword(), passwordModel.getPassword())) {
            log.info("Service: Provided password does not match the existing password, updating password");
            String newEncodedPassword = encoder.encode(PasswordRequestDto.getPassword());
            passwordModel.setOldPassword(passwordModel.getPassword());
            passwordModel.setPassword(newEncodedPassword);
            isChange = true;
        } else {
            log.info("Service: Provided password matches the existing password, no update needed");
        }

        log.info("Service: Password update process completed with status: {}", isChange);
        return isChange;
    }


    private ProviderPassword getProviderPassword(Long providerAccountId) {
        return passwordRepository.findByProviderAccountId(providerAccountId).orElseThrow(() -> new ProviderPhoneException(ErrorCode.PROVIDER_PASSWORD_NOT_FOUND, "Provider password not found by id: " + providerAccountId));
    }

    private void validPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new PasswordException(ErrorCode.INVALID_FIELD, "The 'password' field must be at least 8 characters long.");
        }
    }

}