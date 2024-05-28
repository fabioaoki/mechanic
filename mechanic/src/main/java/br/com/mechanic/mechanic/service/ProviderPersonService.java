package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderPersonException;
import br.com.mechanic.mechanic.mapper.ProviderPersonMapper;
import br.com.mechanic.mechanic.model.ProviderPersonResponseModel;
import br.com.mechanic.mechanic.repository.ProviderPersonRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ProviderPersonService implements ProviderPersonServiceBO {

    private static final Logger logger = LogManager.getLogger(ProviderPersonService.class);

    private final ProviderPersonRepositoryImpl providerPersonRepository;

    @Autowired
    public ProviderPersonService(ProviderPersonRepositoryImpl providerPersonRepository) {
        this.providerPersonRepository = providerPersonRepository;
    }

    @Override
    public ProviderPersonResponseModel save(ProviderPersonRequest personRequest, Long providerAccountId) {
        validPersonField(personRequest);
        logger.info("Service: Saving a new provider person");
        ProviderPerson entity = ProviderPersonMapper.MAPPER.toEntity(personRequest);
        entity.setProviderAccountId(providerAccountId);
        return ProviderPersonMapper.MAPPER.toModel(providerPersonRepository.save(entity));
    }

    private void validPersonField(ProviderPersonRequest personRequest) {
        logger.info("Service: valid person field");
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
