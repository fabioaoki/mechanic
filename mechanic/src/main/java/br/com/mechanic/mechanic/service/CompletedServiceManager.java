package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.*;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.repository.provider.CompletedServiceRepositoryImpl;
import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.RevisionRequest;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
    private final TransactionServiceBO transactionServiceBO;
    private final RevisionServiceBO revisionServiceBO;

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

        log.debug("Starting mapping of completed service request to model");
        CompletedServiceModel completedServiceModel = CompletedServiceMapper.MAPPER.toModel(completedServiceRequest);
        log.debug("Completed mapping of completed service request to model");

        log.debug("Fetching color information for color ID: {}", completedServiceRequest.getColor());
        ColorResponseDto colorResponseDto = colorServiceBO.findByColor(completedServiceRequest.getColor());
        log.info("Retrieved color information for color ID: {}", colorResponseDto.getId());

        log.debug("Fetching vehicle type information for ID: {}", completedServiceRequest.getVehicleTypeId());
        VehicleTypeResponseDto vehicleType = vehicleTypeServiceBO.findById(completedServiceRequest.getVehicleTypeId());
        log.info("Retrieved vehicle type for ID: {}", vehicleType.getName());

        log.debug("Fetching plate information for plate ID: {}", completedServiceRequest.getPlateId());
        PlateResponseDto plate = plateServiceBO.findById(completedServiceRequest.getPlateId());
        log.info("Retrieved plate information");

        log.debug("Fetching model information for model ID: {}", completedServiceRequest.getModelId());
        ModelResponseDto model = modelServiceBO.findById(completedServiceRequest.getModelId());
        log.info("Retrieved model information for model ID: {}", model.getName());

        completedServiceModel.setColorId(colorResponseDto.getId());

        List<Long> completedServiceIds = new ArrayList<>();
        List<CompletedServiceValueResponse> responseList = new ArrayList<>();
        AtomicReference<BigDecimal> equipmentValueRef = new AtomicReference<>(BigDecimal.ZERO);

        log.debug("Processing service value requests");
        completedServiceModel.getServiceValueRequests().forEach(completedServiceValueModel -> {
            log.debug("Fetching employee account details for employee account ID: {}", completedServiceValueModel.getEmployeeAccountId());
            EmployeeAccountResponseDto employeeAccountResponseDto = employeeAccountServiceBO.findById(completedServiceValueModel.getEmployeeAccountId());
            log.info("Retrieved employee account name: {}", employeeAccountResponseDto.getName());

            log.debug("Fetching provider service details for service ID: {}", completedServiceValueModel.getProviderServiceId());
            ProviderServiceResponseDto providerServiceResponseDto = providerServiceBO.findById(completedServiceValueModel.getProviderServiceId());
            log.info("Retrieved provider service details for service ID: {}", providerServiceResponseDto.getService().getIdentifier());

            log.debug("Fetching equipment details for equipment ID: {}", providerServiceResponseDto.getService().getId());
            EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findByProviderServiceIdentifierId(providerServiceResponseDto.getService().getId());

            log.debug("Fetching equipment in details");
            EquipmentInResponseDto equipmentInResponse = equipmentInServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId());
            long equipmentIn = equipmentInResponse.getQuantity() - 1;
            BigDecimal current = equipmentValueRef.get();
            BigDecimal updated = current.add(equipmentInResponse.getAmount());
            equipmentValueRef.set(updated);

            log.debug("Checking equipment availability");
            List<EquipmentOutResponseDto> outList = equipmentOutServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId(), equipmentResponseDto.getCreateDate());

            if (equipmentIn <= outList.size()) {
                if (equipmentInResponse.isFinish()) {
                    log.error("Insufficient equipment for creating completed service");
                    throw new EquipmentException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Missing equipment, it is necessary to include equipment.");
                }
                CompletedServiceValueResponse serviceValueResponse = new CompletedServiceValueResponse();
                serviceValueResponse.setProviderService(providerServiceResponseDto.getService().getIdentifier());
                serviceValueResponse.setEmployeeAccount(employeeAccountResponseDto.getName());
                serviceValueResponse.setAmount(completedServiceValueModel.getAmount());
                responseList.add(serviceValueResponse);

                CompletedService completedServices = CompletedServiceMapper.MAPPER.modelToEntity(completedServiceModel, providerServiceResponseDto, employeeAccountResponseDto, completedServiceValueModel.getAmount());
                CompletedService completedService = completedServiceRepository.save(completedServices);
                completedServiceIds.add(completedService.getId());
                log.info("Saved completed service for employee account ID: {}", employeeAccountResponseDto.getId());
                equipmentOutServiceBO.save(EquipmentOutMapper.MAPPER.completedServiceToEquipmentOut(completedServiceRequest.getProviderAccountId(), completedService.getId(), equipmentResponseDto.getId()));
            }
            if (equipmentIn == outList.size()) {
                equipmentInResponse.setFinish(true);
                log.debug("Updating equipment in status to finish");
                equipmentInServiceBO.updateEquipmentIn(equipmentInResponse.getId(), EquipmentInMapper.MAPPER.mapperUpdate(equipmentInResponse));
            }
        });

        log.debug("Converting completed service IDs list to array");
        Long[] completedServiceIdsArray = completedServiceIds.toArray(new Long[0]);
        log.debug("Starting transaction service save operation");
        TransactionResponse transactionResponse = transactionServiceBO.save(TransactionMapper.MAPPER.completedRequestToTransactionRequest(completedServiceModel, completedServiceIdsArray,
                equipmentValueRef.get().setScale(2, RoundingMode.HALF_UP), model.getModel()));

        log.debug("Processing revisions for service value requests");
        completedServiceModel.getServiceValueRequests().forEach(completedServiceValueModel -> {
            log.debug("Creating revision request for transaction ID: {}", transactionResponse.getId());
            RevisionRequest revisionRequest = RevisionMapper.MAPPER.transactionToRequest(completedServiceValueModel, transactionResponse.getId(), completedServiceRequest.getProviderAccountId(), completedServiceRequest.getClientAccountId());
            revisionServiceBO.save(revisionRequest);
        });

        log.info("Completed service creation process successfully");
        return CompletedServiceMapper.MAPPER.toDto(colorResponseDto.getColor(), providerAccount.getWorkshop(), vehicleType.getName(), plate, model.getModel(), model.getName(), responseList);
    }



    @Override
    public Page<CompletedResponseDtoDefault> findAll(Pageable pageable) {
        log.info("Retrieving list of completedServices");
        return completedServiceRepository.findAll(pageable).map(CompletedServiceMapper.MAPPER::toDtoDefault);
    }

    @Override
    public CompletedResponseDtoDefault findById(Long id) {
        return CompletedServiceMapper.MAPPER.toDtoDefault(getCompletedService(id));
    }

    @Override
    public Page<CompletedResponseDtoDefault> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of completedServices by providerAccountId");
        return completedServiceRepository.findAllByProviderAccountId(pageable, providerAccountId).map(CompletedServiceMapper.MAPPER::toDtoDefault);
    }

    private CompletedService getCompletedService(Long id) {
        return completedServiceRepository.findById(id).orElseThrow(() -> new CompletedServiceException(ErrorCode.ERROR_COMPLETED_SERVICE_NOT_FOUND, "Completed service not found by id: " + id));
    }

    private void validCompletedServiceField(CompletedServiceRequest completedServiceRequest) {
        log.info("Service: valid completed field");
        if (Objects.isNull(completedServiceRequest.getProviderAccountId())) {
            throw new ProviderAccountException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty.");
        }
        if (Objects.isNull(completedServiceRequest.getClientAccountId())) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'clientAccount' field is required and cannot be empty.");
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
        if (completedServiceRequest.getWorkmanshipAmount() == null) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "Workmanship amount cannot be null.");
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