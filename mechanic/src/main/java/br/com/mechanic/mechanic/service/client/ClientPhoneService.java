package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.entity.client.ClientPhone;
import br.com.mechanic.mechanic.exception.ClientPhoneException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.mapper.ClientPhoneMapper;
import br.com.mechanic.mechanic.service.model.ClientPhoneModel;
import br.com.mechanic.mechanic.repository.client.ClientPhoneRepositoryImpl;
import br.com.mechanic.mechanic.service.request.ClientPhoneRequest;
import br.com.mechanic.mechanic.service.response.ClientPhoneResponseDto;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class ClientPhoneService implements ClientPhoneServiceBO {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final ClientPhoneRepositoryImpl phoneRepository;

    @Transactional
    @Override
    public ClientPhoneResponseDto save(ClientPhoneRequest phone, Long clientAccountId) {
        log.info("Service: valid phone field");
        validPhoneField(phone);
        log.info("Service: Saving a new provider phone");
        ClientPhone providerPhone = ClientPhoneMapper.MAPPER.toEntity(phone);
        providerPhone.setClientAccountId(clientAccountId);
        return ClientPhoneMapper.MAPPER.toDto(phoneRepository.save(providerPhone));
    }

    @Override
    public Page<ClientPhoneResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers phones");
        return phoneRepository.findAll(pageable).map(ClientPhoneMapper.MAPPER::toDto);
    }

    @Override
    public ClientPhoneResponseDto findById(Long id) {
        return ClientPhoneMapper.MAPPER.toDto(getPhone(id));
    }

    @Override
    public ClientPhoneResponseDto findByClientAccountId(Long clientAccountId) {
        log.info("Retrieving phone by clientAccount: {}", clientAccountId);

        ClientPhone phone = phoneRepository.findByClientAccountId(clientAccountId)
                .orElseThrow(() -> {
                    log.error("Client account not found by id: {}", clientAccountId);
                    return new ClientPhoneException(ErrorCode.ERROR_CLIENT_ACCOUNT_NOT_FOUND, "Client account not found by id: " + clientAccountId);
                });

        return ClientPhoneMapper.MAPPER.toDto(phone);
    }

    @Override
    public ClientPhoneResponseDto updateClientPhone(Long id, ClientPhoneRequest requestDto) {
        log.info("Service update phone by id: {}", id);
        ClientPhoneModel phoneModel = ClientPhoneMapper.MAPPER.toModel(getPhone(id));
        boolean isChange = updateField(phoneModel, requestDto);
        if (isChange) {
            log.info("Service: Format number");
            phoneModel.setNumber(formatPhoneNumber(phoneModel.getArea(), phoneModel.getNumber()));
            findPhone(phoneModel.getNumber());

            ClientPhone providerPhone = phoneRepository.save(ClientPhoneMapper.MAPPER.modelToEntity(phoneModel));
            return ClientPhoneMapper.MAPPER.toDto(providerPhone);
        }
        throw new ClientPhoneException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider phone.");
    }

    private boolean updateField(ClientPhoneModel phoneModel, ClientPhoneRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getArea()) && !Objects.equals(phoneModel.getArea(), requestDto.getArea())) {
            phoneModel.setArea(requestDto.getArea());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getNumber()) && !Objects.equals(phoneModel.getNumber(), requestDto.getNumber())) {
            phoneModel.setNumber(requestDto.getNumber().trim());
            isChange = true;
        }
        return isChange;
    }

    private ClientPhone getPhone(Long id) {
        return phoneRepository.findById(id).orElseThrow(() -> new ClientPhoneException(ErrorCode.ERROR_CLIENT_PHONE_NOT_FOUND, "Client phone not found by id: " + id));
    }

    private void validPhoneField(ClientPhoneRequest phone) {
        if (phone.getArea() == null || phone.getNumber().isEmpty()) {
            throw new ClientPhoneException(ErrorCode.INVALID_FIELD, "The 'area number and phone number' field is required and cannot be empty.");
        }
        if (!isValidPhoneNumber(phone.getArea(), phone.getNumber())) {
            throw new ClientPhoneException(ErrorCode.INVALID_FIELD, "The 'phone number' field is invalid.");
        }
        log.info("Service: Format number");
        phone.setNumber(formatPhoneNumber(phone.getArea(), phone.getNumber()));
        findPhone(phone.getNumber());
    }

    private void findPhone(String phoneNumber) {
        phoneRepository.findByPhone(phoneNumber)
                .ifPresent(number -> {
                    throw new ClientPhoneException(ErrorCode.ERROR_CREATED_PHONE, "Phone number already registered");
                });
    }

    public static boolean isValidPhoneNumber(Long area, String number) {
        try {
            String phoneNumber = area.toString() + number;
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "BR");
            return phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String formatPhoneNumber(Long area, String number) {
        try {
            String phoneNumber = area.toString() + number;
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "BR"); // Assumindo Brasil como exemplo
            return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            return "+" + area + " " + number;
        }
    }
}