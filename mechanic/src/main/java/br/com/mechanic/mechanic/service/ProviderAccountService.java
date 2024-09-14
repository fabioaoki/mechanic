package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.config.ExcludeFromJacocoGeneratedReport;
import br.com.mechanic.mechanic.entity.provider.ProviderAccount;
import br.com.mechanic.mechanic.entity.provider.ProviderAccountHistory;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.ProviderAccountMapper;
import br.com.mechanic.mechanic.model.ProviderAccountModel;
import br.com.mechanic.mechanic.repository.provider.ProviderAccountHistoryRepositoryImpl;
import br.com.mechanic.mechanic.repository.provider.ProviderAccountRepositoryImpl;
import br.com.mechanic.mechanic.repository.provider.ProviderAccountTypeRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.request.ProviderAccountUpdateRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderAccountService implements ProviderAccountServiceBO {

    private final ProviderAddressServiceBO addressServiceBO;
    private final ProviderPersonServiceBO personServiceBO;
    private final ProviderPhoneServiceBO phoneServiceBO;
    private final PasswordServiceBO passwordServiceBO;

    private final ProviderAccountRepositoryImpl providerAccountRepository;
    private final ProviderAccountTypeRepositoryImpl typeRepository;

    private final ProviderAccountHistoryRepositoryImpl providerAccountHistoryRepository;

    @Override
    public ProviderAccountResponseDto findById(Long id) {
        return ProviderAccountMapper.MAPPER.toDto(getProvider(id));
    }

    private ProviderAccount getProvider(Long id) {
        return providerAccountRepository.findById(id).orElseThrow(() -> new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_NOT_FOUND, "Provider not found by id: " + id));
    }

    @Override
    public Page<ProviderAccountResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers accounts");
        return providerAccountRepository.findAll(pageable).map(ProviderAccountMapper.MAPPER::toDto);
    }

    @Transactional(rollbackFor = {ProviderAccountException.class, ProviderAddressException.class, ProviderPhoneException.class, ProviderAccountTypeException.class})
    @Override
    public ProviderAccountResponseDto save(ProviderAccountRequestDto providerAccountRequest) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        log.info("Service: Creating a new provider account");
        validField(providerAccountRequest);
        ProviderAccount providerAccount = providerAccountRepository.save(ProviderAccountMapper.MAPPER.toEntity(providerAccountRequest));

        ProviderAccountModel accountModel = ProviderAccountMapper.MAPPER.toModel(providerAccount);
        addressServiceBO.save(providerAccountRequest.getAddressRequest(), accountModel.getId());
        ProviderPersonResponseDto personResponseDto = personServiceBO.save(providerAccountRequest.getPersonRequest(), accountModel.getId());
        phoneServiceBO.save(providerAccountRequest.getPhoneRequest(), personResponseDto.getId(), accountModel.getId());
        passwordServiceBO.save(providerAccount.getId(), providerAccountRequest.getPassword(), providerAccountRequest.getLogin());

        return ProviderAccountMapper.MAPPER.toDto(providerAccount);
    }

    @Transactional
    @Override
    public void changeStatus(Long id, ProviderAccountStatusEnum statusEnum) throws ProviderAccountException {
        log.info("Service change status to: {}", statusEnum);
        ProviderAccountModel providerAccountModel = ProviderAccountMapper.MAPPER.toModel(getProvider(id));
        isCancel(providerAccountModel);
        if (!providerAccountModel.getStatus().equals(statusEnum)) {
            providerAccountModel.setStatus(statusEnum);
            providerAccountModel.setLastUpdate(LocalDateTime.now());
            providerAccountRepository.save(ProviderAccountMapper.MAPPER.modelToEntity(providerAccountModel));
            saveHistory(id, statusEnum);
        }
    }

    private static void isCancel(ProviderAccountModel providerAccountModel) {
        if (providerAccountModel.getStatus().equals(ProviderAccountStatusEnum.CANCEL)) {
            throw new ProviderAccountException(ErrorCode.CANNOT_CHANGE_STATUS, "Accounts already canceled");
        }
    }

    @Transactional
    @Override
    public ProviderAccountResponseDto updateProviderAccount(Long id, ProviderAccountUpdateRequestDto requestDto) throws ProviderAccountException {
        log.info("Service update status by id: {}", id);
        validUpdateField(requestDto);
        ProviderAccountModel providerAccountModel = ProviderAccountMapper.MAPPER.toModel(getProvider(id));
        isCancel(providerAccountModel);
        boolean isChange = updateField(providerAccountModel, requestDto);
        if (isChange) {
            ProviderAccount providerAccount = providerAccountRepository.save(ProviderAccountMapper.MAPPER.modelToEntity(providerAccountModel));
            return ProviderAccountMapper.MAPPER.toDto(providerAccount);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    @ExcludeFromJacocoGeneratedReport
    private boolean updateField(ProviderAccountModel providerAccountModel, ProviderAccountUpdateRequestDto requestDto) {
        log.info("Service update field by id: {}", providerAccountModel.getId());
        boolean isChange = false;

        if (requestDto.getWorkshop() != null && !requestDto.getWorkshop().isEmpty() && !requestDto.getWorkshop().equals(providerAccountModel.getWorkshop())) {
            providerAccountModel.setWorkshop(requestDto.getWorkshop());
            isChange = true;
        }

        if (requestDto.getType() != null && !Objects.equals(requestDto.getType(), providerAccountModel.getType())) {
            providerAccountModel.setType(requestDto.getType());
            isChange = true;
        }

        if (requestDto.getCnpj() != null && !requestDto.getCnpj().replaceAll("\\D", "").equals(providerAccountModel.getCnpj())) {
            providerAccountModel.setCnpj(requestDto.getCnpj().replaceAll("\\D", ""));
            isChange = true;
        }

        if (isChange) {
            providerAccountModel.setLastUpdate(LocalDateTime.now());
        }

        return isChange;
    }

    private void validUpdateField(ProviderAccountUpdateRequestDto requestDto) {
        log.info("Service validate field update");
        if (requestDto.getCnpj() != null) {
            providerAccountRepository.findByCnpj(requestDto.getCnpj().replaceAll("\\D", ""))
                    .ifPresent(clientAccount -> {
                        throw new ProviderAccountException(ErrorCode.ERROR_CREATED_CLIENT, "CNPJ already registered");
                    });
        }
        if (requestDto.getType() != null) {
            getProviderType(requestDto.getType());
        }

    }

    private void saveHistory(Long id, ProviderAccountStatusEnum statusEnum) {
        log.info("Service saving change status to: {} by accountId: {}", statusEnum, id);
        ProviderAccountHistory accountHistory = new ProviderAccountHistory();
        accountHistory.setProviderAccountId(id);
        accountHistory.setCreateDate(LocalDateTime.now());
        accountHistory.setStatus(statusEnum);
        providerAccountHistoryRepository.save(accountHistory);
    }

    private void validField(ProviderAccountRequestDto dto) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        log.info("Service: valid provider account type exists");
        getProviderType(dto.getType());
        log.info("Service: valid provider account field");
        validProviderAccount(dto);
    }

    private void getProviderType(Long providerType) {
        typeRepository.getProviderType(providerType).orElseThrow(() -> new ProviderAccountTypeException(ErrorCode.ERROR_PROVIDER_ACCOUNT_TYPE_NOT_FOUND, "Provider type not found by id: " + providerType));
    }

    private void validProviderAccount(ProviderAccountRequestDto dto) {
        if (dto.getWorkshop() == null || dto.getWorkshop().trim().isEmpty()) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'workshop' field is required and cannot be empty.");
        }
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'cnpj' field is required and cannot be empty.");
        }
        providerAccountRepository.findByCnpj(dto.getCnpj().replaceAll("\\D", ""))
                .ifPresent(clientAccount -> {
                    throw new ProviderAccountException(ErrorCode.ERROR_CREATED_CLIENT, "CNPJ already registered");
                });

        validEmail(dto);
        if (dto.getType() == null) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'type' field is required and cannot be empty.");
        }

        log.info("Service: valid provider address account");
        if (dto.getAddressRequest() == null || dto.getAddressRequest().isEmpty()) {
            throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is required and cannot be empty.");
        }
    }

    private void validEmail(ProviderAccountRequestDto dto) {
        if (dto.getLogin() == null || dto.getLogin().trim().isEmpty()) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'email' field is required and cannot be empty.");
        }
        String EMAIL_REGEX = "^(?=.{1,256}$)[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(dto.getLogin().trim());
        if (matcher.matches()) {
            providerAccountRepository.findByLogin(dto.getLogin().trim())
                    .ifPresent(clientAccount -> {
                        throw new ProviderAccountException(ErrorCode.EMAIL_ALREADY_REGISTERED, "Email already registered");
                    });
        }
    }
}
