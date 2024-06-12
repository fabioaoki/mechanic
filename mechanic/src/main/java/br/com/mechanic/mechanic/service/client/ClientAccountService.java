package br.com.mechanic.mechanic.service.client;

import br.com.mechanic.mechanic.entity.client.ClientAccount;
import br.com.mechanic.mechanic.exception.ClientAccountException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.mapper.ClientAccountMapper;
import br.com.mechanic.mechanic.model.ClientAccountModel;
import br.com.mechanic.mechanic.repository.client.ClientAccountRepositoryImpl;
import br.com.mechanic.mechanic.request.*;
import br.com.mechanic.mechanic.response.*;
import br.com.mechanic.mechanic.service.ColorServiceBO;
import br.com.mechanic.mechanic.service.vehicle.MarcServiceBO;
import br.com.mechanic.mechanic.service.vehicle.PlateServiceBO;
import br.com.mechanic.mechanic.service.vehicle.VehicleServiceBO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
@Service
public class ClientAccountService implements ClientAccountServiceBO {

    private final ClientAccountRepositoryImpl clientAccountRepository;

    private ClientAddressServiceBO addressServiceBO;
    private ClientPersonServiceBO personServiceBO;
    private ClientPhoneServiceBO phoneServiceBO;
    private PlateServiceBO plateServiceBO;
    private MarcServiceBO marcServiceBO;
    private ColorServiceBO colorServiceBO;
    private VehicleServiceBO vehicleServiceBO;

    @Transactional
    @Override
    public ClientAccountResponseDto save(ClientAccountRequest clientAccountRequest) {
        log.info("Start saving client account.");

        ClientAccountModel accountModel = ClientAccountMapper.MAPPER.toModel(clientAccountRequest);
        accountModel.setName(formatName(accountModel.getName().trim()));
        formatCpf(accountModel);
        accountModel.setRg(accountModel.getRg().replaceAll("[^\\d]", ""));

        log.info("Checking if CPF or RG is already registered: CPF={}, RG={}", accountModel.getCpf(), accountModel.getRg());
        clientAccountRepository.findByCpfOrRg(accountModel.getCpf(), accountModel.getRg()).ifPresent(clientAccount -> {
            log.error("CPF or RG already registered: CPF={}, RG={}", accountModel.getCpf(), accountModel.getRg());
            throw new ClientAccountException(ErrorCode.ERROR_CREATED_CLIENT, "CPF or RG already registered");
        });

        log.info("Saving new client account to the database.");
        ClientAccount clientAccount = clientAccountRepository.save(ClientAccountMapper.MAPPER.modelToEntity(accountModel));
        log.info("Client account saved with ID: {}", clientAccount.getId());

        log.info("Saving client address.");
        ClientAddressResponseDto addressResponseDto = addressServiceBO.save(clientAccountRequest.getAddress(), clientAccount.getId());
        log.info("Client address saved.");

        log.info("Saving client person details.");
        ClientPersonResponseDto personResponseDto = personServiceBO.save(clientAccountRequest.getPerson(), clientAccount.getId());
        log.info("Client person details saved.");

        log.info("Saving client phone details.");
        ClientPhoneResponseDto phoneResponseDto = phoneServiceBO.save(clientAccountRequest.getPhone(), clientAccount.getId());
        log.info("Client phone details saved.");

        clientAccountRequest.getVehicles().forEach(vehicleRequest -> {
            if (Objects.isNull(vehicleRequest.getPlate()) ||
                    Objects.isNull(vehicleRequest.getColorId()) ||
                    Objects.isNull(vehicleRequest.getMarcId())) {
                throw new ClientAccountException(ErrorCode.INVALID_FIELD, "Mismatch in sizes of lists: Plates, Marcs, and Colors must have the same number of elements.");
            }
        });

        log.info("Processing and saving plates.");
        List<PlateRequest> plateRequests = clientAccountRequest.getVehicles().stream()
                .map(VehicleRequest::getPlate)
                .collect(Collectors.toList());
        List<PlateResponseDto> plateResponseList = plateServiceBO.save(plateRequests, clientAccount.getId());
        log.info("Plates processed and saved.");

        log.info("Processing and saving marcs.");
        List<Long> marcIds = clientAccountRequest.getVehicles().stream()
                .map(VehicleRequest::getMarcId)
                .collect(Collectors.toList());
        List<MarcResponseDto> marcResponseList = new ArrayList<>();
        marcIds.forEach(marcId -> {
            MarcResponseDto marcResponseDto = marcServiceBO.findById(marcId);
            marcResponseList.add(marcResponseDto);
            log.info("Marc saved with ID: {}", marcResponseDto.getId());
        });

        log.info("Processing and saving colors.");
        List<Long> colorIds = clientAccountRequest.getVehicles().stream()
                .map(VehicleRequest::getColorId)
                .collect(Collectors.toList());
        List<ColorResponseDto> colorResponseList = new ArrayList<>();
        colorIds.forEach(colorId -> {
            ColorResponseDto colorResponseDto = colorServiceBO.findById(colorId);
            colorResponseList.add(colorResponseDto);
            log.info("Color saved with ID: {}", colorResponseDto.getId());
        });

//        if (plateResponseList.size() != marcResponseList.size() || plateResponseList.size() != colorResponseList.size()) {
//            log.error("Mismatch in sizes of lists: Plates={}, Marcs={}, Colors={}",
//                    plateResponseList.size(), marcResponseList.size(), colorResponseList.size());
//            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "Mismatch in sizes of lists: Plates, Marcs, and Colors must have the same number of elements.");
//        }
        log.info("Processing and saving vehicles.");

        for (int i = 0; i < plateResponseList.size(); i++) {
            log.info("Saving vehicle with Plate ID: {}, Marc ID: {}, Color ID: {}",
                    plateResponseList.get(i).getId(),
                    marcResponseList.get(i).getId(),
                    colorIds.get(i));

            SaveVehicleRequest saveVehicleRequest = SaveVehicleRequest.builder()
                    .clientAccountId(clientAccount.getId())
                    .marcId(marcResponseList.get(i).getId())
                    .colorId(colorIds.get(i))
                    .plateId(plateResponseList.get(i).getId())
                    .build();

            vehicleServiceBO.save(saveVehicleRequest, true);
            log.info("Vehicle saved successfully with Plate ID: {}", plateResponseList.get(i).getId());
        }

        log.info("Client account saved successfully with all related entities.");
        return ClientAccountMapper.MAPPER.toDtoMaster(clientAccount, personResponseDto, addressResponseDto, phoneResponseDto, plateResponseList, marcResponseList, colorResponseList);
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

    @Override
    public Page<ClientAccountResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of clients");
        return clientAccountRepository.findAll(pageable).map(ClientAccountMapper.MAPPER::toDto);
    }

    @Override
    public ClientAccountResponseDto findById(Long id) {
        return ClientAccountMapper.MAPPER.toDto(getClientAccount(id));
    }

    @Override
    public ClientAccountResponseDto updateClientAccount(Long id, ClientAccountUpdateRequest requestDto) {
        log.info("Service update client account by id: {}", id);
        ClientAccountModel accountModel = ClientAccountMapper.MAPPER.toModel(getClientAccount(id));
        boolean isChange = updateField(accountModel, requestDto);
        if (isChange) {
            ClientAccount clientAccount = clientAccountRepository.save(ClientAccountMapper.MAPPER.modelToEntity(accountModel));
            return ClientAccountMapper.MAPPER.toDtoUpdate(clientAccount);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    private boolean updateField(ClientAccountModel employeeAccountModel, ClientAccountUpdateRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getBirthDate()) && employeeAccountModel.getBirthDate() != requestDto.getBirthDate()) {
            employeeAccountModel.setBirthDate(requestDto.getBirthDate());
            validBirthDate(requestDto.getBirthDate());
            isChange = true;
        }
        if (Objects.nonNull(requestDto.getName()) && !requestDto.getName().isEmpty()) {
            isValidName(requestDto.getName());
            if (!employeeAccountModel.getName().equalsIgnoreCase(requestDto.getName())) {
                employeeAccountModel.setName(formatName(requestDto.getName().trim()));
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getCpf()) && !requestDto.getCpf().isEmpty()) {
            if (!employeeAccountModel.getCpf().equals(requestDto.getCpf())) {
                employeeAccountModel.setCpf(requestDto.getCpf());
                formatCpf(employeeAccountModel);
                clientAccountRepository.findByCpf(employeeAccountModel.getCpf()).ifPresent(clientAccount -> {
                    throw new ClientAccountException(ErrorCode.ERROR_OPDATE_CLIENT, "CPF already registered");
                });
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getRg()) && !requestDto.getRg().isEmpty()) {
            validRg(requestDto.getRg());
            if (!employeeAccountModel.getRg().equals(requestDto.getRg())) {
                employeeAccountModel.setRg(requestDto.getRg().replaceAll("[^\\d]", ""));
                clientAccountRepository.findByRg(employeeAccountModel.getRg()).ifPresent(clientAccount -> {
                    throw new ClientAccountException(ErrorCode.ERROR_OPDATE_CLIENT, "RG already registered");
                });
                isChange = true;
            }
        }

        return isChange;
    }

    private static void isValidName(String name) {
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field must contain only letters and spaces.");
        }
        if (name.split("\\s+").length < 2) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is not full.");
        }
    }


    private ClientAccount getClientAccount(Long id) {
        return clientAccountRepository.findById(id).orElseThrow(() -> new ClientAccountException(ErrorCode.ERROR_CLIENT_ACCOUNT_NOT_FOUND, "Client account not found by id: " + id));
    }

    private void validClientAccountField(ClientAccountRequest clientAccountRequest) {
        log.info("Service: valid client field");
        if (clientAccountRequest.getPerson().getName() == null) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' field is required.");
        }
        validBirthDate(clientAccountRequest.getPerson().getBirthDate());

        if (clientAccountRequest.getCpf().isEmpty() || clientAccountRequest.getCpf().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'cpf' field is required.");
        }
        if (clientAccountRequest.getPerson().getName().isEmpty() || clientAccountRequest.getPerson().getName().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'name' field is required.");
        }
        isValidName(clientAccountRequest.getPerson().getName());

        if (clientAccountRequest.getRg().isEmpty() || clientAccountRequest.getRg().trim().isEmpty()) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field is required.");
        }
        validRg(clientAccountRequest.getRg());
    }

    private static void validRg(String rg) {
        String numericRg = rg.replaceAll("[^\\d]", "");

        if (numericRg.length() != 9) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field must be 9 digits long.");
        }

        if (!rg.matches("\\d{9}") && !rg.matches("\\d{2}\\.\\d{3}\\.\\d{3}-\\d{1}")) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'rg' field must be in the format XX.XXX.XXX-X or contain only numbers.");
        }
    }

    private static void validBirthDate(LocalDate birthDate) {
        LocalDate today = LocalDate.now();

        if (birthDate.isAfter(today)) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'birthDate' cannot be in the future.");
        }

        int age = Period.between(birthDate, today).getYears();
        if (age < 18) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The person must be at least 18 years old.");
        }
    }

    private static void formatCpf(ClientAccountModel employeeAccountModel) {
        employeeAccountModel.setCpf(employeeAccountModel.getCpf().replaceAll("\\D", ""));
        if (employeeAccountModel.getCpf().length() != 11) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "Invalid CPF length.");
        }
    }
}