package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.EmployeeAccount;
import br.com.mechanic.mechanic.exception.EmployeeAccountException;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.EmployeeAccountMapper;
import br.com.mechanic.mechanic.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.repository.EmployeeAccountRepositoryImpl;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.EmployeeAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class EmployeeAccountService implements EmployeeAccountServiceBO {

    private final EmployeeAccountRepositoryImpl employeeAccountRepository;
    private final ProviderAccountServiceBO accountServiceBO;

    @Override
    public EmployeeAccountResponseDto save(EmployeeAccountRequest employeeAccountRequest) {
        validEmployeeField(employeeAccountRequest);
        log.info("Service: Saving a new employee");
        EmployeeAccount entity = EmployeeAccountMapper.MAPPER.toEntity(employeeAccountRequest);
        ProviderAccountResponseDto accountResponseDto = getProviderAccountResponseDto(entity.getProviderAccountId());
        return EmployeeAccountMapper.MAPPER.toDto(employeeAccountRepository.save(entity), accountResponseDto);
    }

    @Override
    public Page<EmployeeAccountResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of employees");
        return employeeAccountRepository.findAll(pageable).map(EmployeeAccountMapper.MAPPER::toDtoPage);
    }

    @Override
    public EmployeeAccountResponseDto findById(Long id) {
        EmployeeAccount employee = getEmployee(id);
        ProviderAccountResponseDto accountResponseDto = getProviderAccountResponseDto(employee.getProviderAccountId());
        return EmployeeAccountMapper.MAPPER.toDto(employee, accountResponseDto);
    }

    private ProviderAccountResponseDto getProviderAccountResponseDto(Long providerAccountId) {
        return accountServiceBO.findById(providerAccountId);
    }

    @Override
    public EmployeeAccountResponseDto updateEmployeeAccount(Long id, EmployeeAccountRequest requestDto) {
        log.info("Service update person by id: {}", id);
        EmployeeAccountModel employeeAccountModel = EmployeeAccountMapper.MAPPER.toModel(getEmployee(id));
        boolean isChange = updateField(employeeAccountModel, requestDto);
        if (isChange) {
            ProviderAccountResponseDto accountResponseDto = accountServiceBO.findById(requestDto.getProviderAccountId());
            EmployeeAccount providerPerson = employeeAccountRepository.save(EmployeeAccountMapper.MAPPER.modelToEntity(employeeAccountModel));
            return EmployeeAccountMapper.MAPPER.toDto(providerPerson, accountResponseDto);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    private boolean updateField(EmployeeAccountModel employeeAccountModel, EmployeeAccountRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getBirthDate()) && employeeAccountModel.getBirthDate() != requestDto.getBirthDate()) {
            employeeAccountModel.setBirthDate(requestDto.getBirthDate());
            validBirthDate(requestDto.getBirthDate());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getName()) && !requestDto.getName().isEmpty()) {
            isValidName(requestDto.getName());
            if (!employeeAccountModel.getName().equalsIgnoreCase(requestDto.getName())) {
                employeeAccountModel.setName(requestDto.getName().trim());
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getProviderAccountId()) && requestDto.getProviderAccountId() != 0L) {
            if (!employeeAccountModel.getProviderAccountId().equals(requestDto.getProviderAccountId())) {
                employeeAccountModel.setProviderAccountId(requestDto.getProviderAccountId());
                isChange = true;
            }
        }
        return isChange;
    }

    private static void isValidName(String name) {
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'name' field must contain only letters and spaces.");
        }
        if (name.split("\\s+").length < 2) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is not full.");
        }
    }


    private EmployeeAccount getEmployee(Long id) {
        return employeeAccountRepository.findById(id).orElseThrow(() -> new EmployeeAccountException(ErrorCode.ERROR_PROVIDER_PERSON_NOT_FOUND, "Provider person not found by id: " + id));
    }

    private void validEmployeeField(EmployeeAccountRequest employeeAccountRequest) {
        log.info("Service: valid employee field");
        if (employeeAccountRequest.getBirthDate() == null) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }
        validBirthDate(employeeAccountRequest.getBirthDate());

        if (employeeAccountRequest.getName().isEmpty() || employeeAccountRequest.getName().trim().isEmpty()) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is required.");
        }
        isValidName(employeeAccountRequest.getName());

        if (Objects.isNull(employeeAccountRequest.getProviderAccountId()) || employeeAccountRequest.getProviderAccountId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty or zero.");
        }
        if (Objects.isNull(employeeAccountRequest.getRole())) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'role' field is required and cannot be empty or zero.");
        }
    }

    private static void validBirthDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' cannot be in the future.");
        }

        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The person must be at least 18 years old.");
        }
    }
}
