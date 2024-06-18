package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.CompletedServices;
import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.CompletedServiceMapper;
import br.com.mechanic.mechanic.mapper.EmployeeAccountMapper;
import br.com.mechanic.mechanic.mapper.EquipmentInMapper;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.model.EmployeeAccountModel;
import br.com.mechanic.mechanic.repository.provider.CompletedServiceRepositoryImpl;
import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.EmployeeAccountRequest;
import br.com.mechanic.mechanic.response.*;
import br.com.mechanic.mechanic.service.vehicle.ModelServiceBO;
import br.com.mechanic.mechanic.service.vehicle.PlateServiceBO;
import br.com.mechanic.mechanic.service.vehicle.VehicleTypeServiceBO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class CompletedServiceManager implements CompletedServiceManagerBO {

    private final CompletedServiceRepositoryImpl completedServiceRepository;
    private final ColorServiceBO colorServiceBO;
    private final EmployeeAccountServiceBO employeeAccountServiceBO;
    private final ProviderServiceBO providerServiceBO;
    private final ProviderAccountServiceBO accountServiceBO;
    private final VehicleTypeServiceBO vehicleTypeServiceBO;
    private final PlateServiceBO plateServiceBO;
    private final ModelServiceBO modelServiceBO;
    private final EquipmentServiceBO equipmentServiceBO;
    private final EquipmentInServiceBO equipmentInServiceBO;
    private final EquipmentOutServiceBO equipmentOutServiceBO;

    @Transactional
    @Override
    public CompletedResponseDto save(CompletedServiceRequest completedServiceRequest) {

        log.info("Starting to create completed service for provider account ID: {}", completedServiceRequest.getProviderAccountId());
        validCompletedServiceField(completedServiceRequest);

        ProviderAccountResponseDto providerAccount = accountServiceBO.findById(completedServiceRequest.getProviderAccountId());
        log.info("Retrieved provider account status: {}", providerAccount.getStatus());
        if (providerAccount.getStatus().equals(ProviderAccountStatusEnum.CANCEL)) {
            log.error("Provider account status is canceled");
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled.");
        }


        CompletedServiceModel completedServiceModel = CompletedServiceMapper.MAPPER.toModel(completedServiceRequest);
        log.debug("Mapped completed service request to model");

        ColorResponseDto colorResponseDto = colorServiceBO.findByColor(completedServiceRequest.getColor());
        log.info("Retrieved color information for color ID: {}", colorResponseDto.getId());


        VehicleTypeResponseDto vehicleType = vehicleTypeServiceBO.findById(completedServiceRequest.getVehicleTypeId());
        log.info("Retrieved vehicle type for ID: {}", vehicleType.getName());

        PlateResponseDto plate = plateServiceBO.findById(completedServiceRequest.getPlateId());
        log.info("Retrieved plate information");

        ModelResponseDto model = modelServiceBO.findById(completedServiceRequest.getModelId());
        log.info("Retrieved model information for model ID: {}", model.getName());

        completedServiceModel.setColorId(colorResponseDto.getId());

        List<Long> completedServiceIds = new ArrayList<>();

        List<CompletedServiceValueResponse> responseList = new ArrayList<>();
        completedServiceModel.getServiceValueRequests().forEach(completedServiceValueModel -> {
            EmployeeAccountResponseDto employeeAccountResponseDto = employeeAccountServiceBO.findById(completedServiceValueModel.getEmployeeAccountId());
            log.info("Retrieved employee account name: {}", employeeAccountResponseDto.getName());

            ProviderServiceResponseDto providerServiceResponseDto = providerServiceBO.findById(completedServiceValueModel.getProviderServiceId());
            log.info("Retrieved provider service details for service ID: {}", providerServiceResponseDto.getService().getIdentifier());
            EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findByProviderServiceIdentifierId(providerServiceResponseDto.getService().getId());

            EquipmentInResponseDto equipmentInResponse = equipmentInServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId());
            Long equipmentIn = equipmentInResponse.getQuantity() - 1;

            List<EquipmentOutResponseDto> outList = equipmentOutServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId(), equipmentResponseDto.getCreateDate());

            if (equipmentIn <= outList.size()) {
                CompletedServiceValueResponse serviceValueResponse = new CompletedServiceValueResponse();
                serviceValueResponse.setProviderService(providerServiceResponseDto.getService().getIdentifier());
                serviceValueResponse.setEmployeeAccount(employeeAccountResponseDto.getName());
                serviceValueResponse.setAmount(completedServiceValueModel.getAmount());
                responseList.add(serviceValueResponse);

                CompletedServices completedServices = CompletedServiceMapper.MAPPER.modelToEntity(completedServiceModel, providerServiceResponseDto, employeeAccountResponseDto, completedServiceValueModel.getAmount());
                CompletedServices completedService = completedServiceRepository.save(completedServices);
                completedServiceIds.add(completedService.getId());
                log.info("Saved completed service for employee account ID: {}", employeeAccountResponseDto.getId());
            }
            if (equipmentIn == outList.size()) {
                equipmentInResponse.setFinish(true);
                equipmentInServiceBO.updateEquipmentIn(equipmentInResponse.getId(), EquipmentInMapper.MAPPER.mapperUpdate(equipmentInResponse));
            }

        });


        log.info("Completed service creation process successfully");
        return CompletedServiceMapper.MAPPER.toDto(colorResponseDto.getColor(), providerAccount.getWorkshop(), vehicleType.getName(), plate, model.getModel(), model.getName(), responseList);
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


    private static void formatCpf(EmployeeAccountModel employeeAccountModel) {
        employeeAccountModel.setCpf(employeeAccountModel.getCpf().replaceAll("\\D", ""));
        if (employeeAccountModel.getCpf().length() != 11) {
            throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "Invalid CPF length.");
        }
    }

    @Override
    public Page<EmployeeAccountResponseDtoPage> findAll(Pageable pageable) {
        log.info("Retrieving list of employees");
        return completedServiceRepository.findAll(pageable).map(EmployeeAccountMapper.MAPPER::toDtoPage);
    }

    @Override
    public EmployeeAccountResponseDto findById(Long id) {
        EmployeeAccount employee = getEmployee(id);
        ProviderAccountResponseDto accountResponseDto = getProviderAccountResponseDto(employee.getProviderAccountId());
        return EmployeeAccountMapper.MAPPER.toDto(employee, accountResponseDto);
    }

    private ProviderAccountResponseDto getProviderAccountResponseDto(Long providerAccountId) {
        return accountServiceBO.findById(providerAccountId);
    }

    @Override
    public EmployeeAccountResponseDto updateEmployeeAccount(Long id, EmployeeAccountRequest requestDto) {
        log.info("Service update employee account by id: {}", id);
        EmployeeAccountModel employeeAccountModel = EmployeeAccountMapper.MAPPER.toModel(getEmployee(id));
        boolean isChange = updateField(employeeAccountModel, requestDto);
        if (isChange) {
            ProviderAccountResponseDto accountResponseDto = accountServiceBO.findById(employeeAccountModel.getProviderAccountId());
            EmployeeAccount providerPerson = completedServiceRepository.save(EmployeeAccountMapper.MAPPER.modelToEntity(employeeAccountModel));
            return EmployeeAccountMapper.MAPPER.toDto(providerPerson, accountResponseDto);
        }
        throw new ProviderAccountException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider account.");
    }

    private boolean updateField(EmployeeAccountModel employeeAccountModel, EmployeeAccountRequest requestDto) {
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
                completedServiceRepository.findByCpf(employeeAccountModel.getCpf()).ifPresent(clientAccount -> {
                    throw new EmployeeAccountException(ErrorCode.ERROR_CREATED_EMPLOYEE, "CPF already registered");
                });
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getProviderAccountId()) && requestDto.getProviderAccountId() != 0L) {
            if (!employeeAccountModel.getProviderAccountId().equals(requestDto.getProviderAccountId())) {
                employeeAccountModel.setProviderAccountId(requestDto.getProviderAccountId());
                isChange = true;
            }
        }
        if (Objects.nonNull(requestDto.getRole())) {
            if (!employeeAccountModel.getRole().equals(requestDto.getRole().toString())) {
                employeeAccountModel.setRole(requestDto.getRole().toString());
                isChange = true;
            }
        }
        return isChange;
    }


    private EmployeeAccount getEmployee(Long id) {
        return completedServiceRepository.findById(id).orElseThrow(() -> new EmployeeAccountException(ErrorCode.ERROR_PROVIDER_PERSON_NOT_FOUND, "Provider person not found by id: " + id));
    }

    private void validCompletedServiceField(CompletedServiceRequest completedServiceRequest) {
        log.info("Service: valid completed field");
        if (Objects.isNull(completedServiceRequest.getProviderAccountId())) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty.");
        }
        if (Objects.isNull(completedServiceRequest.getColor()) || completedServiceRequest.getColor().trim().isEmpty()) {
            throw new ColorException(ErrorCode.INVALID_FIELD, "The 'color' field is required and cannot be empty.");
        }
        if (Objects.isNull(completedServiceRequest.getModelId())) {
            throw new ModelException(ErrorCode.INVALID_FIELD, "The 'model' field is required and cannot be empty.");
        }
        if (Objects.isNull(completedServiceRequest.getPlateId())) {
            throw new ColorException(ErrorCode.INVALID_FIELD, "The 'plateId' field is required and cannot be empty.");
        }
        if (Objects.isNull(completedServiceRequest.getVehicleTypeId())) {
            throw new VehicleTypeException(ErrorCode.INVALID_FIELD, "The 'vehicleTypeId' field is required and cannot be empty.");
        }
        if (completedServiceRequest.getServiceValueRequests() == null || completedServiceRequest.getServiceValueRequests().isEmpty()) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'serviceValueRequest' field is required and cannot be empty.");
        }
        completedServiceRequest.getServiceValueRequests().forEach(completedServiceValueRequest -> {
            if (Objects.isNull(completedServiceValueRequest.getProviderServiceId())) {
                throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'providerServiceId' field is required and cannot be empty.");
            }
            if (Objects.isNull(completedServiceValueRequest.getEmployeeAccountId())) {
                throw new EmployeeAccountException(ErrorCode.INVALID_FIELD, "The 'employeeAccountId' field is required and cannot be empty.");
            }
            if (Objects.isNull(completedServiceValueRequest.getAmount()) || completedServiceValueRequest.getAmount().compareTo(BigDecimal.ZERO) == 0) {
                throw new CompletedServiceException(ErrorCode.INVALID_FIELD, "The 'amount' field is required and cannot be empty or zero.");
            }
        });
    }

}
