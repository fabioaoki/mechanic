package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderPersonException;
import br.com.mechanic.mechanic.mapper.ProviderPersonMapper;
import br.com.mechanic.mechanic.model.ProviderPersonResponseModel;
import br.com.mechanic.mechanic.repository.ProviderPersonRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
@AllArgsConstructor
@Log4j2
@Service
public class ProviderPersonService implements ProviderPersonServiceBO {

    private final ProviderPersonRepositoryImpl providerPersonRepository;

    @Override
    public ProviderPersonResponseModel save(ProviderPersonRequest personRequest, Long providerAccountId) {
        validPersonField(personRequest);
        log.info("Service: Saving a new provider person");
        ProviderPerson entity = ProviderPersonMapper.MAPPER.toEntity(personRequest);
        entity.setProviderAccountId(providerAccountId);
        return ProviderPersonMapper.MAPPER.toModel(providerPersonRepository.save(entity));
    }

    private void validPersonField(ProviderPersonRequest personRequest) {
        log.info("Service: valid person field");
        if (personRequest.getBirthDate() == null) {
            throw new ProviderPersonException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }

        LocalDate birthDate = personRequest.getBirthDate();
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
