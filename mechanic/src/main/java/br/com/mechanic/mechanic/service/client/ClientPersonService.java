package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import br.com.mechanic.mechanic.exception.ClientPersonException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ClientAccountException;
import br.com.mechanic.mechanic.mapper.ClientPersonMapper;
import br.com.mechanic.mechanic.model.ClientPersonModel;
import br.com.mechanic.mechanic.repository.client.ClientPersonRepositoryImpl;
import br.com.mechanic.mechanic.request.ClientPersonRequest;
import br.com.mechanic.mechanic.response.ClientPersonResponseDto;
import br.com.mechanic.mechanic.service.client.ClientPersonServiceBO;
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
public class ClientPersonService implements ClientPersonServiceBO {

    private final ClientPersonRepositoryImpl clientPersonRepository;

    @Override
    public ClientPersonResponseDto save(ClientPersonRequest personRequest, Long clientAccountId) {
        validPersonField(personRequest);
        log.info("Service: Saving a new client person");
        ClientPerson entity = ClientPersonMapper.MAPPER.toEntity(personRequest);
        entity.setClientAccountId(clientAccountId);
        return ClientPersonMapper.MAPPER.toDto(clientPersonRepository.save(entity));
    }

    @Override
    public Page<ClientPersonResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of client persons");
        return clientPersonRepository.findAll(pageable).map(ClientPersonMapper.MAPPER::toDto);
    }

    @Override
    public ClientPersonResponseDto findById(Long id) {
        return ClientPersonMapper.MAPPER.toDto(getPerson(id));
    }

    @Override
    public ClientPersonResponseDto updateClientPerson(Long id, ClientPersonRequest requestDto) {
        log.info("Service update person by id: {}", id);
        ClientPersonModel personModel = ClientPersonMapper.MAPPER.toModel(getPerson(id));
        boolean isChange = updateField(personModel, requestDto);
        if (isChange) {
            ClientPerson clientPerson = clientPersonRepository.save(ClientPersonMapper.MAPPER.modelToEntity(personModel));
            return ClientPersonMapper.MAPPER.toDto(clientPerson);
        }
        throw new ClientAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the client account.");
    }

    private boolean updateField(ClientPersonModel personModel, ClientPersonRequest requestDto) {
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
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The 'name' field must contain only letters and spaces.");
        }
        if (name.split("\\s+").length < 2) {
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The 'name' field is not full.");
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


    private ClientPerson getPerson(Long id) {
        return clientPersonRepository.findById(id).orElseThrow(() -> new ClientPersonException(ErrorCode.ERROR_PROVIDER_PERSON_NOT_FOUND, "Provider person not found by id: " + id));
    }

    private void validPersonField(ClientPersonRequest personRequest) {
        log.info("Service: valid person field");
        if (personRequest.getBirthDate() == null) {
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }
        if (personRequest.getName().isEmpty() || personRequest.getName().trim().isEmpty()) {
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The 'name' field is required.");
        }

        isValidName(personRequest.getName());
        validBirthDate(personRequest.getBirthDate());
    }

    private static void validBirthDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The 'birthDate' cannot be in the future.");
        }

        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new ClientPersonException(ErrorCode.INVALID_FIELD, "The person must be at least 18 years old.");
        }
    }
}
