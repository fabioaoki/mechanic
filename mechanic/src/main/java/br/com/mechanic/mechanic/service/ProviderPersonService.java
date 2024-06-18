package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.ProviderPerson;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderPersonException;
import br.com.mechanic.mechanic.mapper.ProviderPersonMapper;
import br.com.mechanic.mechanic.model.ProviderPersonModel;
import br.com.mechanic.mechanic.repository.provider.ProviderPersonRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPersonUpdateRequest;
import br.com.mechanic.mechanic.response.ProviderPersonResponseDto;
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
public class ProviderPersonService implements ProviderPersonServiceBO {

    private final ProviderPersonRepositoryImpl providerPersonRepository;

    @Override
    public ProviderPersonResponseDto save(ProviderPersonRequest personRequest, Long providerAccountId) {
        validPersonField(personRequest);
        log.info("Service: Saving a new provider person");
        ProviderPerson entity = ProviderPersonMapper.MAPPER.toEntity(personRequest);
        entity.setProviderAccountId(providerAccountId);
        entity.setName(formatName(entity.getName()));
        return ProviderPersonMapper.MAPPER.toDto(providerPersonRepository.save(entity));
    }


    @Override
    public Page<ProviderPersonResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers persons");
        return providerPersonRepository.findAll(pageable).map(ProviderPersonMapper.MAPPER::toDto);
    }

    @Override
    public ProviderPersonResponseDto findById(Long id) {
        return ProviderPersonMapper.MAPPER.toDto(getPerson(id));
    }

    @Override
    public ProviderPersonResponseDto updateProviderPerson(Long id, ProviderPersonUpdateRequest requestDto) {
        log.info("Service update person by id: {}" , id);
        ProviderPersonModel personModel = ProviderPersonMapper.MAPPER.toModel(getPerson(id));
        boolean isChange = updateField(personModel, requestDto);
        if (isChange) {
            ProviderPerson providerPerson = providerPersonRepository.save(ProviderPersonMapper.MAPPER.modelToEntity(personModel));
            return ProviderPersonMapper.MAPPER.toDto(providerPerson);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    private boolean updateField(ProviderPersonModel personModel, ProviderPersonUpdateRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getBirthDate()) && personModel.getBirthDate() != requestDto.getBirthDate()) {
            personModel.setBirthDate(requestDto.getBirthDate());
            validBirthDate(requestDto.getBirthDate());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getName()) && !requestDto.getName().isEmpty()) {
            isValidName(requestDto.getName());
            if (!personModel.getName().equalsIgnoreCase(requestDto.getName())) {
                personModel.setName(formatName(requestDto.getName().trim()));
                isChange = true;
            }
        }
        return isChange;
    }

    private static void isValidName(String name) {
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'name' field must contain only letters and spaces.");
        }
        if (name.split("\\s+").length < 2) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'name' field is not full.");
        }
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


    private ProviderPerson getPerson(Long id) {
        return providerPersonRepository.findById(id).orElseThrow(() -> new ProviderPersonException(ErrorCode.ERROR_PROVIDER_PERSON_NOT_FOUND, "Provider person not found by id: " + id));
    }

    private void validPersonField(ProviderPersonRequest personRequest) {
        log.info("Service: valid person field");
        if (personRequest.getBirthDate() == null) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }
        if (personRequest.getName().isEmpty() || personRequest.getName().trim().isEmpty()) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'name' field is required.");
        }

        isValidName(personRequest.getName());
        validBirthDate(personRequest.getBirthDate());
    }

    private static void validBirthDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'birthDate' cannot be in the future.");
        }

        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The person must be at least 18 years old.");
        }
    }
}
