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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderPhoneService implements ProviderPhoneServiceBO {

    private static final Logger logger = LogManager.getLogger(ProviderPhoneService.class);

    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final ProviderPhoneRepositoryImpl phoneRepository;

    @Autowired
    public ProviderPhoneService(ProviderPhoneRepositoryImpl phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    @Override
    public void save(List<ProviderPhoneRequest> phones, Long personId, Long providerAccountId) {
        logger.info("Service: valid phone field");
        validPhoneField(phones);
        logger.info("Service: Saving a new provider phone");
        phones.forEach(phone -> {
            ProviderPhone providerPhone = ProviderPhoneMapper.MAPPER.toEntity(phone);
            providerPhone.setProviderAccountId(providerAccountId);
            providerPhone.setProviderPersonId(personId);
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
            logger.info("Service: Format number");
            phone.setNumber(formatPhoneNumber(phone.getArea(), phone.getNumber()));
            phoneRepository.findByPhone(phone.getNumber())
                    .ifPresent(number -> {
                        throw new ProviderPhoneException(ErrorCode.ERROR_CREATED_PHONE, "Phone number already registered");
                    });
        });
    }

    public static boolean isValidPhoneNumber(Long area, String number) {
        try {
            // Construa o número de telefone no formato local, pois estamos passando a região "BR"
            String phoneNumber = area.toString() + number;
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "BR"); // Assumindo Brasil como exemplo
            return phoneNumberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static String formatPhoneNumber(Long area, String number) {
        try {
            // Construa o número de telefone no formato local, pois estamos passando a região "BR"
            String phoneNumber = area.toString() + number;
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, "BR"); // Assumindo Brasil como exemplo
            return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        } catch (NumberParseException e) {
            return "+" + area + " " + number;
        }
    }
}
