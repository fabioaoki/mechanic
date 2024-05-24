package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.*;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.ProviderAccountMapper;
import br.com.mechanic.mechanic.mapper.ProviderAddressMapper;
import br.com.mechanic.mechanic.mapper.ProviderPersonMapper;
import br.com.mechanic.mechanic.mapper.ProviderPhoneMapper;
import br.com.mechanic.mechanic.model.ProviderAccountModel;
import br.com.mechanic.mechanic.repository.*;
import br.com.mechanic.mechanic.request.ProviderAccountRequestDto;
import br.com.mechanic.mechanic.request.ProviderAddressRequest;
import br.com.mechanic.mechanic.request.ProviderPersonRequest;
import br.com.mechanic.mechanic.request.ProviderPhoneRequest;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class ProviderAccountService implements ProviderAccountServiceBO {

    private static final Logger logger = LogManager.getLogger(ProviderAccountService.class);

    private final ProviderAccountRepositoryImpl providerAccountRepository;
    private final ProviderAddressRepositoryImpl addressRepository;

    private final ProviderPersonRepositoryImpl providerPersonRepository;

    private final ProviderPhoneRepositoryImpl phoneRepository;

    private final ProviderAccountTypeRepositoryImpl typeRepository;

    private final ProviderAccountHistoryRepositoryImpl providerAccountHistoryRepository;
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();


    @Autowired
    public ProviderAccountService(ProviderAccountRepositoryImpl providerAccountRepository, ProviderAddressRepositoryImpl addressRepository, ProviderPersonRepositoryImpl providerPersonRepository, ProviderPhoneRepositoryImpl phoneRepository,
                                  ProviderAccountTypeRepositoryImpl typeRepository, ProviderAccountHistoryRepositoryImpl providerAccountHistoryRepository) {
        this.providerAccountRepository = providerAccountRepository;
        this.addressRepository = addressRepository;
        this.providerPersonRepository = providerPersonRepository;
        this.phoneRepository = phoneRepository;
        this.typeRepository = typeRepository;
        this.providerAccountHistoryRepository = providerAccountHistoryRepository;
    }

    @Override
    public ProviderAccountResponseDto findById(Long id) {
        return ProviderAccountMapper.MAPPER.toDto(getProvider(id));
    }

    private ProviderAccount getProvider(Long id) {
        return providerAccountRepository.findById(id).orElseThrow(() -> new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_NOT_FOUND, "Provider not found by id: " + id));
    }

    @Override
    public Page<ProviderAccountResponseDto> findAll(Pageable pageable) {
        logger.info("Retrieving list of providers accounts");
        return providerAccountRepository.findAll(pageable).map(ProviderAccountMapper.MAPPER::toDto);
    }

    @Transactional(rollbackFor = {ProviderAccountException.class, ProviderAddressException.class, ProviderPhoneException.class, ProviderAccountTypeException.class})
    @Override
    public ProviderAccountResponseDto save(ProviderAccountRequestDto providerAccountRequest) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        logger.info("Service: Creating a new provider account");
        validField(providerAccountRequest);
        ProviderAccount providerAccount = providerAccountRepository.save(ProviderAccountMapper.MAPPER.toEntity(providerAccountRequest));
        saveProviderAddressRequest(providerAccountRequest.getAddressRequest(), providerAccount.getId());
        ProviderPerson providerPerson = savePerson(providerAccountRequest.getPersonRequest(), providerAccount.getId());
        savePhone(providerAccountRequest.getPhoneRequest(), providerPerson.getId(), providerAccount.getId());

        return ProviderAccountMapper.MAPPER.toDto(providerAccount);
    }

    private void savePhone(List<ProviderPhoneRequest> phones, Long personId, Long providerAccountId) {
        logger.info("Service: Saving a new provider phone");
        phones.forEach(phone -> {
            ProviderPhone providerPhone = ProviderPhoneMapper.MAPPER.toEntity(phone);
            providerPhone.setProviderAccountId(providerAccountId);
            providerPhone.setProviderPersonId(personId);
            phoneRepository.save(providerPhone);
        });
    }

    private ProviderPerson savePerson(ProviderPersonRequest personRequest, Long providerAccountId) {
        logger.info("Service: Saving a new provider person");
        ProviderPerson entity = ProviderPersonMapper.MAPPER.toEntity(personRequest);
        entity.setProviderAccountId(providerAccountId);
        return providerPersonRepository.save(entity);
    }

    private void saveProviderAddressRequest(List<ProviderAddressRequest> addressRequestList, Long providerAccountId) {
        logger.info("Service: saving a new provider address");
        addressRequestList.forEach(addressRequest -> {
            ProviderAddress entity = ProviderAddressMapper.MAPPER.toEntity(addressRequest);
            entity.setProviderAccountId(providerAccountId);
            addressRepository.save(entity);
        });

    }

    @Transactional
    @Override
    public void changeStatus(Long id, ProviderAccountStatusEnum statusEnum) throws ProviderAccountException {
        logger.info("Service change status to: {}", statusEnum);
        ProviderAccountModel providerAccountModel = ProviderAccountMapper.MAPPER.toModel(getProvider(id));
        if (!providerAccountModel.getStatus().equals(statusEnum)) {
            providerAccountModel.setStatus(statusEnum);
            providerAccountModel.setLastUpdate(LocalDateTime.now());
            providerAccountRepository.save(ProviderAccountMapper.MAPPER.modelToEntity(providerAccountModel));
            saveHistory(id, statusEnum);
        }
    }

    private void saveHistory(Long id, ProviderAccountStatusEnum statusEnum) {
        logger.info("Service saving change status to: {} by accountId: {}", statusEnum, id);
        ProviderAccountHistory accountHistory = new ProviderAccountHistory();
        accountHistory.setProviderAccountId(id);
        accountHistory.setCreateDate(LocalDateTime.now());
        accountHistory.setStatus(statusEnum);
        providerAccountHistoryRepository.save(accountHistory);
    }

    private void validField(ProviderAccountRequestDto dto) throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
        logger.info("Service: valid provider account type exists");
        typeRepository.getProviderType(dto.getType()).orElseThrow(() -> new ProviderAccountTypeException(ErrorCode.ERROR_PROVIDER_ACCOUNT_TYPE_NOT_FOUND, "Provider type not found by id: " + dto.getType()));
        logger.info("Service: valid provider account field");
        validProviderAccount(dto);
        logger.info("Service: valid address field");
        validAddressField(dto.getAddressRequest());
        logger.info("Service: valid phone field");
        validPhoneField(dto.getPhoneRequest());
        logger.info("Service: valid person field");
        validPersonField(dto.getPersonRequest());
    }

    private void validPersonField(ProviderPersonRequest personRequest) {
        logger.info("Service: Validate person field");
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

    private void validProviderAccount(ProviderAccountRequestDto dto) {
        if (dto.getWorkshop() == null || dto.getWorkshop().trim().isEmpty()) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'workshop' field is required and cannot be empty.");
        }
        if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'cnpj' field is required and cannot be empty.");
        }
        providerAccountRepository.findByCnpj(dto.getCnpj().replaceAll("\\D", ""))
                .ifPresent(clientAccount -> {
                    throw new ProviderAccountException(ErrorCode.ERROR_CREATED_CLIENT, "CNPJ already registered");
                });
        if (dto.getType() == null) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'type' field is required and cannot be empty.");
        }

        logger.info("Service: valid provider address account");
        if (dto.getAddressRequest() == null || dto.getAddressRequest().isEmpty()) {
            throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'address' field is required and cannot be empty.");
        }
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

    private void validAddressField(List<ProviderAddressRequest> addressRequestList) throws ProviderAddressException {
        addressRequestList.forEach(addressRequest -> {
            if (addressRequest.getCity() == null || addressRequest.getCity().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'city' field is required and cannot be empty.");
            }
            if (addressRequest.getStreet() == null || addressRequest.getStreet().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'street' field is required and cannot be empty.");
            }
            if (addressRequest.getZipCode() == null || addressRequest.getZipCode().trim().isEmpty()) {
                throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'zipcode' field is required and cannot be empty.");
            }
            addressRepository.findByAddress(addressRequest.getCity(), addressRequest.getStreet(), addressRequest.getZipCode().replaceAll("\\s", ""))
                    .ifPresent(address -> {
                        throw new ProviderAddressException(ErrorCode.ERROR_CREATED_ADDRESS, "address already registered");
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
