package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.ProviderPhone;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.mapper.ProviderPhoneMapper;
import br.com.mechanic.mechanic.model.ProvidePhoneModel;
import br.com.mechanic.mechanic.repository.provider.ProviderPhoneRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.response.ProviderPhoneResponseDto;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderPhoneService implements ProviderPhoneServiceBO {

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final ProviderPhoneRepositoryImpl phoneRepository;

    @Override
    public void save(List<ProviderPhoneRequest> phones, Long personId, Long providerAccountId) {
        log.info("Service: valid phone field");
        validPhoneField(phones);
        log.info("Service: Saving a new provider phone");
        phones.forEach(phone -> {
            ProviderPhone providerPhone = ProviderPhoneMapper.MAPPER.toEntity(phone);
            providerPhone.setProviderAccountId(providerAccountId);
            providerPhone.setProviderPersonId(personId);
            phoneRepository.save(providerPhone);
        });
    }

    @Override
    public Page<ProviderPhoneResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of providers phones");
        return phoneRepository.findAll(pageable).map(ProviderPhoneMapper.MAPPER::toDto);
    }

    @Override
    public ProviderPhoneResponseDto findById(Long id) {
        return ProviderPhoneMapper.MAPPER.toDto(getPhone(id));
    }

    @Override
    public Page<ProviderPhoneResponseDto> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of phones by provider");
        Page<ProviderPhone> phones = phoneRepository.findByProviderAccountId(pageable, providerAccountId);

        if (phones.isEmpty()) {
            throw new ProviderPhoneException(ErrorCode.ERROR_PROVIDER_ACCOUNT_NOT_FOUND, "Provider account not found by id: " + providerAccountId);
        }

        return phones.map(ProviderPhoneMapper.MAPPER::toDto);
    }

    @Override
    public ProviderPhoneResponseDto updateProviderPhone(Long id, ProviderPhoneRequest requestDto) {
        log.info("Service update phone by id: {}", id);
        ProvidePhoneModel phoneModel = ProviderPhoneMapper.MAPPER.toModel(getPhone(id));
        boolean isChange = updateField(phoneModel, requestDto);
        if (isChange) {
            log.info("Service: Format number");
            phoneModel.setNumber(formatPhoneNumber(phoneModel.getArea(), phoneModel.getNumber()));
            findPhone(phoneModel.getNumber());

            ProviderPhone providerPhone = phoneRepository.save(ProviderPhoneMapper.MAPPER.modelToEntity(phoneModel));
            return ProviderPhoneMapper.MAPPER.toDto(providerPhone);
        }
        throw new ProviderPhoneException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider phone.");
    }

    private boolean updateField(ProvidePhoneModel phoneModel, ProviderPhoneRequest requestDto) {
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

    private ProviderPhone getPhone(Long id) {
        return phoneRepository.findById(id).orElseThrow(() -> new ProviderPhoneException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Provider phone not found by id: " + id));
    }

    private void validPhoneField(List<ProviderPhoneRequest> phones) {
        phones.forEach(phone -> {
            if (phone.getArea() == null || phone.getNumber().isEmpty()) {
                throw new ProviderPhoneException(ErrorCode.INVALID_FIELD, "The 'area number and phone number' field is required and cannot be empty.");
            }
            if (!isValidPhoneNumber(phone.getArea())) {
                throw new ProviderPhoneException(ErrorCode.INVALID_FIELD, "The 'phone number' field is invalid.");
            }
            log.info("Service: Format number");
            phone.setNumber(formatPhoneNumber(phone.getArea(), phone.getNumber()));
            findPhone(phone.getNumber());
        });
    }

    private void findPhone(String phoneNumber) {
        phoneRepository.findByPhone(phoneNumber)
                .ifPresent(number -> {
                    throw new ProviderPhoneException(ErrorCode.ERROR_CREATED_PHONE, "Phone number already registered");
                });
    }

    public static boolean isValidPhoneNumber(Long area) {
        return isValidAreaCode(area.toString());
    }

    public static boolean isValidAreaCode(String areaCode) {
        return areaCode.matches("^[1-9][0-9]$");
}

    public static String formatPhoneNumber(Long area, String number) {
        try {
            String phoneNumber = area.toString() + number;
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "BR");
            return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            System.out.println("NumberParseException was thrown: " + e.toString());
            return "+" + area + " " + number; // Fallback format
        }
    }
}