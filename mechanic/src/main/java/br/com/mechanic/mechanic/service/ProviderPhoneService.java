package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderPhone;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderPhoneException;
import br.com.mechanic.mechanic.mapper.ProviderPhoneMapper;
import br.com.mechanic.mechanic.repository.ProviderPhoneRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private void validPhoneField(List<ProviderPhoneRequest> phones) {
        phones.forEach(phone -> {
            if (phone.getArea() == null || phone.getNumber().isEmpty()) {
                throw new ProviderPhoneException(ErrorCode.INVALID_FIELD, "The 'area number and phone number' field is required and cannot be empty.");
            }
            if (!isValidPhoneNumber(phone.getArea(), phone.getNumber())) {
                throw new ProviderPhoneException(ErrorCode.INVALID_FIELD, "The 'phone number' field is invalid.");
            }
            log.info("Service: Format number");
            phone.setNumber(formatPhoneNumber(phone.getArea(), phone.getNumber()));
            phoneRepository.findByPhone(phone.getNumber())
                    .ifPresent(number -> {
                        throw new ProviderPhoneException(ErrorCode.ERROR_CREATED_PHONE, "Phone number already registered");
                    });
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