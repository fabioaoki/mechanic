package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.entity.client.ClientAccount;
import br.com.mechanic.mechanic.exception.ClientAccountException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.ClientAccountMapper;
import br.com.mechanic.mechanic.model.ClientAccountModel;
import br.com.mechanic.mechanic.repository.client.ClientAccountRepositoryImpl;
import br.com.mechanic.mechanic.request.ClientAccountRequest;
import br.com.mechanic.mechanic.request.ClientAccountUpdateRequest;
import br.com.mechanic.mechanic.response.ClientAccountResponseDto;
import br.com.mechanic.mechanic.response.ClientAddressResponseDto;
import br.com.mechanic.mechanic.response.ClientPersonResponseDto;
import br.com.mechanic.mechanic.response.ClientPhoneResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class ClientAccountService implements ClientAccountServiceBO {

    private final ClientAccountRepositoryImpl clientAccountRepository;

    private ClientAddressServiceBO addressServiceBO;
    private ClientPersonServiceBO personServiceBO;
    private ClientPhoneServiceBO phoneServiceBO;
    @Transactional
    @Override
    public ClientAccountResponseDto save(ClientAccountRequest clientAccountRequest) {
        validClientAccountField(clientAccountRequest);

        ClientAccountModel accountModel = ClientAccountMapper.MAPPER.toModel(clientAccountRequest);
        accountModel.setName(formatName(accountModel.getName().trim()));
        formatCpf(accountModel);
        accountModel.setRg(accountModel.getRg().replaceAll("[^\\d]", ""));

        clientAccountRepository.findByCpfOrRg(accountModel.getCpf(), accountModel.getRg()).ifPresent(clientAccount -> {
            throw new ClientAccountException(ErrorCode.ERROR_CREATED_CLIENT, "CPF or RG already registered");
        });
        log.info("Service: Saving a new client account");
        ClientAccount entity = ClientAccountMapper.MAPPER.modelToEntity(accountModel);
        ClientAccount clientAccount = clientAccountRepository.save(ClientAccountMapper.MAPPER.modelToEntity(accountModel));
        ClientAddressResponseDto addressResponseDto = addressServiceBO.save(clientAccountRequest.getAddress(), clientAccount.getId());
        ClientPersonResponseDto clientPersonResponseDto = personServiceBO.save(clientAccountRequest.getPerson(), clientAccount.getId());
        ClientPhoneResponseDto phoneResponseDto = phoneServiceBO.save(clientAccountRequest.getPhone(), clientAccount.getId());

        return ClientAccountMapper.MAPPER.toDto(clientAccountRepository.save(entity), accountResponseDto);
    }

    public String formatName(String name) {

        String[] words = name.split("\\s+");

        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formattedName.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return formattedName.toString().trim();
    }

    @Override
    public Page<ClientAccountResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of clients");
        return clientAccountRepository.findAll(pageable).map(ClientAccountMapper.MAPPER::toDto);
    }

    @Override
    public ClientAccountResponseDto findById(Long id) {
        return ClientAccountMapper.MAPPER.toDto(getClientAccount(id));
    }

    @Override
    public ClientAccountResponseDto updateClientAccount(Long id, ClientAccountUpdateRequest requestDto) {
        log.info("Service update client account by id: {}", id);
        ClientAccountModel accountModel = ClientAccountMapper.MAPPER.toModel(getClientAccount(id));
        boolean isChange = updateField(accountModel, requestDto);
        if (isChange) {
            ClientAccount clientAccount = clientAccountRepository.save(ClientAccountMapper.MAPPER.modelToEntity(accountModel));
            return ClientAccountMapper.MAPPER.toDto(clientAccount);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    private boolean updateField(ClientAccountModel employeeAccountModel, ClientAccountUpdateRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getBirthDate()) && employeeAccountModel.getBirthDate() != requestDto.getBirthDate()) {
            employeeAccountModel.setBirthDate(requestDto.getBirthDate());
            validBirthDate(requestDto.getBirthDate());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getName()) && !requestDto.getName().isEmpty()) {
            isValidName(requestDto.getName());
            if (!employeeAccountModel.getName().equalsIgnoreCase(requestDto.getName())) {
                employeeAccountModel.setName(formatName(requestDto.getName().trim()));
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getCpf()) && !requestDto.getCpf().isEmpty()) {
            if (!employeeAccountModel.getCpf().equals(requestDto.getCpf())) {
                employeeAccountModel.setCpf(requestDto.getCpf());
                formatCpf(employeeAccountModel);
                clientAccountRepository.findByCpf(employeeAccountModel.getCpf()).ifPresent(clientAccount -> {
                    throw new ClientAccountException(ErrorCode.ERROR_OPDATE_CLIENT, "CPF already registered");
                });
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getRg()) && !requestDto.getRg().isEmpty()) {
            validRg(requestDto.getRg());
            if (!employeeAccountModel.getRg().equals(requestDto.getRg())) {
                employeeAccountModel.setRg(requestDto.getRg().replaceAll("[^\\d]", ""));
                clientAccountRepository.findByRg(employeeAccountModel.getRg()).ifPresent(clientAccount -> {
                    throw new ClientAccountException(ErrorCode.ERROR_OPDATE_CLIENT, "RG already registered");
                });
                isChange = true;
            }
        }

        return isChange;
    }

    private static void isValidName(String name) {
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field must contain only letters and spaces.");
        }
        if (name.split("\\s+").length < 2) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is not full.");
        }
    }


    private ClientAccount getClientAccount(Long id) {
        return clientAccountRepository.findById(id).orElseThrow(() -> new ClientAccountException(ErrorCode.ERROR_CLIENT_ACCOUNT_NOT_FOUND, "Client account not found by id: " + id));
    }

    private void validClientAccountField(ClientAccountRequest clientAccountRequest) {
        log.info("Service: valid client field");
        if (clientAccountRequest.getBirthDate() == null) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }
        validBirthDate(clientAccountRequest.getBirthDate());

        if (clientAccountRequest.getCpf().isEmpty() || clientAccountRequest.getCpf().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'cpf' field is required.");
        }
        if (clientAccountRequest.getName().isEmpty() || clientAccountRequest.getName().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is required.");
        }
        isValidName(clientAccountRequest.getName());

        if (clientAccountRequest.getRg().isEmpty() || clientAccountRequest.getRg().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field is required.");
        }
        validRg(clientAccountRequest.getRg());
    }

    private static void validRg(String rg) {
        String numericRg = rg.replaceAll("[^\\d]", "");

        if (numericRg.length() != 9) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field must be 9 digits long.");
        }

        if (!rg.matches("\\d{9}") && !rg.matches("\\d{2}\\.\\d{3}\\.\\d{3}-\\d{1}")) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field must be in the format XX.XXX.XXX-X or contain only numbers.");
        }
    }

    private static void validBirthDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' cannot be in the future.");
        }

        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The person must be at least 18 years old.");
        }
    }

    private static void formatCpf(ClientAccountModel employeeAccountModel) {
        employeeAccountModel.setCpf(employeeAccountModel.getCpf().replaceAll("\\D", ""));
        if (employeeAccountModel.getCpf().length() != 11) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "Invalid CPF length.");
        }
    }
}
