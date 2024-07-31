package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.CompletedServiceMapper;
import br.com.mechanic.mechanic.mapper.EquipmentOutMapper;
import br.com.mechanic.mechanic.mapper.RevisionMapper;
import br.com.mechanic.mechanic.mapper.TransactionMapper;
import br.com.mechanic.mechanic.model.CompletedServiceModel;
import br.com.mechanic.mechanic.repository.provider.CompletedServiceRepositoryImpl;
import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.EquipmentInRequest;
import br.com.mechanic.mechanic.request.ReversalCompletedServiceRequest;
import br.com.mechanic.mechanic.request.RevisionRequest;
import br.com.mechanic.mechanic.response.*;
import br.com.mechanic.mechanic.service.vehicle.ModelServiceBO;
import br.com.mechanic.mechanic.service.vehicle.PlateServiceBO;
import br.com.mechanic.mechanic.service.vehicle.VehicleServiceBO;
import br.com.mechanic.mechanic.service.vehicle.VehicleTypeServiceBO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    private final VehicleServiceBO vehicleServiceBO;

    @Transactional
    @Override
    public CompletedResponseDto save(CompletedServiceRequest completedServiceRequest) {

        log.info("Starting to create completed service for provider account ID: {}", completedServiceRequest.getProviderAccountId());
        validCompletedServiceField(completedServiceRequest);

        ProviderAccountResponseDto providerAccount = accountServiceBO.findById(completedServiceRequest.getProviderAccountId());
        log.info("Retrieved provider account status: {}", providerAccount.getStatus());
        if (providerAccount.getStatus().equals(ProviderAccountStatusEnum.CANCEL) || providerAccount.getStatus().equals(ProviderAccountStatusEnum.INITIAL_BLOCK)) {
            log.error("Provider account status is canceled");
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled or block.");
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
        if (!plate.getClientAccountId().equals(completedServiceRequest.getClientAccountId())) {
            throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "PlateAccountId different from clientAccountId.");
        }

        VehicleResponseDto vehiclePlateResponse = vehicleServiceBO.findByPlateId(completedServiceRequest.getPlateId());
        if (!vehiclePlateResponse.getModelId().equals(completedServiceRequest.getModelId()) | !colorResponseDto.getColor().equals(formatColor(completedServiceRequest.getColor()))
                | !vehiclePlateResponse.getVehicleTypeId().equals(completedServiceRequest.getVehicleTypeId())) {
            throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Model, vehicleTypeId or color is different from clientAccountId.");
        }

        log.info("Retrieved plate information");

        log.debug("Fetching model information for model ID: {}", completedServiceRequest.getModelId());
        ModelResponseDto model = modelServiceBO.findById(completedServiceRequest.getModelId());
        log.info("Retrieved model information for model ID: {}", model.getName());

        completedServiceModel.setColorId(colorResponseDto.getId());

        List<Long> completedServiceIds = new ArrayList<>();
        List<CompletedServiceValueResponse> responseList = new ArrayList<>();
        AtomicReference<BigDecimal> equipmentValueRef = new AtomicReference<>(BigDecimal.ZERO);

        log.debug("Processing service value requests");
        completedServiceModel.getServiceValueRequests().forEach(serviceValueModelRequest -> {
            LocalDate expectedReturnDate = null;
            long revisionId = 0L;
            if (Objects.nonNull(serviceValueModelRequest.getCompletedServiceId()) && serviceValueModelRequest.getCompletedServiceId() != 0L) {

                CompletedService completedService = completedServiceRepository.findById(serviceValueModelRequest.getCompletedServiceId()).orElseThrow(
                        () -> new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Completed service id: " + serviceValueModelRequest.getCompletedServiceId() + " not found."));

                if (serviceValueModelRequest.getQuantityRevised() > completedService.getQuantity() || serviceValueModelRequest.getQuantityRevised() > (completedService.getQuantity() - completedService.getQuantityRevised())) {
                    throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Amount of revision greater than what exists.");
                }

                long totalRevised = serviceValueModelRequest.getQuantityRevised() + completedService.getQuantityRevised();
                completedService.setQuantityRevised(totalRevised);
                completedServiceRepository.save(completedService);

                TransactionResponse transactionResponse = transactionServiceBO.findById(completedService.getTransactionId());
                if (!transactionResponse.getClientAccountId().equals(completedServiceRequest.getClientAccountId())) {
                    throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Return transaction is not from the same clientAccountId.");
                }
                RevisionResponse revisionResponse = revisionServiceBO.findByCompletedServiceId(completedService.getId());
                if (revisionResponse.getReturnDate() != null && revisionResponse.isFinish()) {
                    throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Return already made before today's date.");
                }

                expectedReturnDate = calculateExpectedReturn(revisionResponse.getStartDate(), serviceValueModelRequest.getEndDate());
                boolean isFinish = completedService.getQuantity().equals(totalRevised);
                long quantityRevised = serviceValueModelRequest.getQuantity();
                revisionId = revisionResponse.getId();
                revisionServiceBO.updateRevision(revisionResponse.getId(), LocalDate.now(), isFinish, quantityRevised);
            }

            log.debug("Fetching employee account details for employee account ID: {}", serviceValueModelRequest.getEmployeeAccountId());
            EmployeeAccountResponseDto employeeAccountResponseDto = employeeAccountServiceBO.findById(serviceValueModelRequest.getEmployeeAccountId());
            log.info("Retrieved employee account name: {}", employeeAccountResponseDto.getName());

            if (!completedServiceModel.getProviderAccountId().equals(employeeAccountResponseDto.getProviderAccountId().getId())) {
                throw new ProviderAccountException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "ProviderAccount different from employer providerAccount.");
            }

            log.debug("Fetching provider service details for service ID: {}", serviceValueModelRequest.getProviderServiceId());
            ProviderServiceResponseDto providerServiceResponseDto = providerServiceBO.findById(serviceValueModelRequest.getProviderServiceId());
            log.info("Retrieved provider service details for service ID: {}", providerServiceResponseDto.getService().getIdentifier());

            if (!providerServiceResponseDto.getVehicleTypeId().equals(completedServiceRequest.getVehicleTypeId())) {
                throw new ProviderServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Type of equipment not compatible with vehicle.");
            }

            log.debug("Fetching equipment details for equipment ID: {}", providerServiceResponseDto.getService().getId());
            EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findByProviderServiceIdentifierId(providerServiceResponseDto.getService().getId());

            log.debug("Fetching equipment in details");
            EquipmentInResponseDto equipmentInResponse = equipmentInServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId());
            long equipmentIn = equipmentInResponse.getQuantity() - 1;
            BigDecimal current = equipmentValueRef.get();
            BigDecimal updated = current.add(
                    equipmentInResponse.getAmount()
                            .multiply(new BigDecimal(serviceValueModelRequest.getQuantity()))
            );
            equipmentValueRef.set(updated);

            log.debug("Checking equipment availability");
            List<EquipmentOutResponseDto> outList = equipmentOutServiceBO.findByProviderAccountAndEquipmentId(completedServiceRequest.getProviderAccountId(), equipmentResponseDto.getId(), equipmentResponseDto.getCreateDate());

            if (outList.size() <= equipmentIn) {
                if (equipmentInResponse.isFinish()) {
                    log.error("Insufficient equipment for creating completed service");
                    throw new EquipmentException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Missing equipment, it is necessary to include equipment.");
                }
                CompletedServiceValueResponse serviceValueResponse = new CompletedServiceValueResponse();
                serviceValueResponse.setProviderService(providerServiceResponseDto.getService().getIdentifier());
                serviceValueResponse.setEmployeeAccount(employeeAccountResponseDto.getName());
                serviceValueResponse.setStartDate(LocalDate.now());
                serviceValueResponse.setEndDate(serviceValueModelRequest.getEndDate());
                serviceValueResponse.setAmount(equipmentInResponse.getAmount().multiply(BigDecimal.valueOf(serviceValueModelRequest.getQuantity())));
                serviceValueResponse.setQuantity(serviceValueModelRequest.getQuantity());
                serviceValueResponse.setUnitPrice(equipmentInResponse.getAmount());
                serviceValueResponse.setMileageForInspection(serviceValueModelRequest.getMileageForInspection());
                responseList.add(serviceValueResponse);


                long quantity = serviceValueModelRequest.getQuantity();

                CompletedService completedServices = CompletedServiceMapper.MAPPER.modelToEntity(
                        completedServiceModel,
                        providerServiceResponseDto,
                        employeeAccountResponseDto,
                        equipmentInResponse.getAmount()
                        , quantity, 0
                );
                CompletedService completedServiceEntity = completedServiceRepository.save(completedServices);


                log.debug("Creating revision request for completedServiceId ID: {}", completedServiceEntity.getId());
                RevisionRequest revisionRequest = RevisionMapper.MAPPER.transactionToRequest(serviceValueModelRequest, completedServiceEntity.getId(), completedServiceRequest.getProviderAccountId(), completedServiceRequest.getClientAccountId(), completedServiceRequest.getMileage(), revisionId, expectedReturnDate);
                revisionServiceBO.save(revisionRequest);

                completedServiceIds.add(completedServiceEntity.getId());
                log.info("Saved completed service for employee account ID: {}", employeeAccountResponseDto.getId());


                boolean isFinish = false;
                for (int i = 0; i < quantity; i++) {
                    if (isFinish) {
                        throw new CompletedServiceException(ErrorCode.ERROR_CREATED_COMPLETED_SERVICE, "Missing equipment, it is necessary to include equipment.");
                    }
                    equipmentOutServiceBO.save(
                            EquipmentOutMapper.MAPPER.completedServiceToEquipmentOut(
                                    completedServiceRequest.getProviderAccountId(),
                                    completedServiceEntity.getId(),
                                    equipmentResponseDto.getId()
                            )
                    );

                    if (equipmentIn == outList.size() + i) {
                        equipmentInResponse.setFinish(true);
                        isFinish = true;
                        log.debug("Updating equipment in status to finish");
                        equipmentInServiceBO.finish(equipmentInResponse.getId());
                    }
                }
            }
        });

        log.debug("Converting completed service IDs list to String");
        String completedIds = completedServiceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        log.debug("Starting transaction service save operation");
        BigDecimal totalAmount = equipmentValueRef.get().setScale(2, RoundingMode.HALF_UP).add(completedServiceRequest.getWorkmanshipAmount());
        TransactionResponse transactionResponse = transactionServiceBO.save(TransactionMapper.MAPPER.completedRequestToTransactionRequest(completedServiceModel, completedIds,
                totalAmount, model.getModel()));

        completedServiceRepository.setTransactionIds(completedServiceIds, transactionResponse.getId());

        log.info("Completed service creation process successfully");
        return CompletedServiceMapper.MAPPER.toDto(colorResponseDto.getColor(), providerAccount.getWorkshop(), vehicleType.getName(), plate, model.getModel(), model.getName(), responseList, completedServiceModel.getInstallments(), totalAmount, completedServiceRequest.getMileage());
    }

    private LocalDate calculateExpectedReturn(LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        LocalDate expectedDate = LocalDate.now().plusDays(daysBetween);

        return expectedDate.equals(endDate) ? expectedDate : null;
    }

    public static String formatColor(String color) {
        color = color.trim().toLowerCase();
        return color.substring(0, 1).toUpperCase() + color.substring(1).toLowerCase();
    }

    @Override
    public List<EmployeeServiceCountDto> getCompletedServiceCountByEmployee(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching completed service count for providerAccountId: {}, from: {}, to: {}", providerAccountId, startDate, endDate);
        accountServiceBO.findById(providerAccountId);
        return completedServiceRepository.countFirstCompletedServiceByEmployee(providerAccountId, startDate, endDate);
    }

    @Override
    public List<ProviderServiceCountDto> getCompletedServiceCountByProviderService(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching completed service count for providerAccountId: {}, from: {}, to: {}", providerAccountId, startDate, endDate);
        accountServiceBO.findById(providerAccountId);
        return completedServiceRepository.countFirstCompletedServiceByProviderService(providerAccountId, startDate, endDate);
    }

    @Override
    public List<ProviderServiceCountGroupByDateDto> countFirstCompletedServiceByProviderServiceGroupByDate(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching completed service count for providerAccountId: {}, from: {}, to: {}, group by date", providerAccountId, startDate, endDate);
        accountServiceBO.findById(providerAccountId);
        return completedServiceRepository.countFirstCompletedServiceByProviderServiceGroupByDate(providerAccountId, startDate, endDate);
    }

    @Override
    public List<ProviderServiceCountCompletedServiceGroupByDateDto> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching completed service count for providerAccountId: {}, from: {}, to: {}, group by date", providerAccountId, startDate, endDate);
        accountServiceBO.findById(providerAccountId);
        return completedServiceRepository.countCompletedServicesByVehicleTypeIdAndOptionalDate(providerAccountId, startDate, endDate);
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
    public Page<CompletedResponseByProviderAccountDto> findAllByProviderAccountId(Long providerAccountId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        log.info("Retrieving list of completed services by providerAccountId: {}", providerAccountId);
        ProviderAccountResponseDto providerAccount = accountServiceBO.findById(providerAccountId);

        return completedServiceRepository.findAllByProviderAccountId(pageable, providerAccountId, startDate, endDate)
                .map(completedResponseDtoDefault -> {
                    EmployeeAccountResponseDto employeeResponse = employeeAccountServiceBO.findById(completedResponseDtoDefault.getEmployeeAccountId());
                    ProviderServiceResponseDto serviceResponseDto = providerServiceBO.findById(completedResponseDtoDefault.getProviderServiceId());
                    EquipmentResponseDto equipmentResponseDto = equipmentServiceBO.findByProviderServiceIdentifierId(serviceResponseDto.getService().getId());
                    EquipmentInResponseDto equipmentInResponse = equipmentInServiceBO.findByProviderAccountAndEquipmentId(providerAccountId, equipmentResponseDto.getId());
                    TransactionResponse transactionResponse = transactionServiceBO.findById(completedResponseDtoDefault.getTransactionId());
                    ModelResponseDto modelResponseDto = modelServiceBO.findById(completedResponseDtoDefault.getModelId());
                    VehicleTypeResponseDto vehicleType = vehicleTypeServiceBO.findById(completedResponseDtoDefault.getVehicleTypeId());

                    return CompletedServiceMapper.MAPPER.byProviderAccountId(providerAccount, employeeResponse.getName(), serviceResponseDto,
                            equipmentResponseDto.getName(), equipmentInResponse.getAmount(), transactionResponse.getWorkmanshipAmount(),
                            modelResponseDto, vehicleType.getName(), completedResponseDtoDefault.getCreateDate().toLocalDate(), completedResponseDtoDefault.getQuantity());
                });
    }

    @Override
    public Page<CompletedResponseDto> findAllByClientAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of completedServices by clientAccountId");

        return null;
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
        if (Objects.isNull(completedServiceRequest.getInstallments())) {
            throw new ClientAccountException(ErrorCode.INVALID_FIELD, "The 'installments' field is required and cannot be empty.");
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
            if (Objects.isNull(completedServiceValueRequest.getQuantity())) {
                throw new CompletedServiceException(ErrorCode.INVALID_FIELD, "The 'quantity' field is required and cannot be empty.");
            }
            if (Objects.isNull(completedServiceValueRequest.getEndDate())) {
                throw new CompletedServiceException(ErrorCode.INVALID_FIELD, "The 'endDate' field is required.");
            }
        });
    }

    @Transactional
    @Override
    public void reversalCompletedService(Long providerAccountId, ReversalCompletedServiceRequest reversalRequest) {
        log.info("Service: find completedService by provider accountId: {}, and completedServiceId: {}", providerAccountId, reversalRequest.getCompletedServiceId());
        CompletedResponseDtoDefault completedResponseDtoDefault = findById(reversalRequest.getCompletedServiceId());

        if (isValidTransaction(completedResponseDtoDefault, reversalRequest, providerAccountId)) {
            RevisionResponse revisionResponse = revisionServiceBO.findByCompletedServiceId(completedResponseDtoDefault.getId());
            TransactionResponse transactionResponse = transactionServiceBO.findById(completedResponseDtoDefault.getTransactionId());

            if (!revisionResponse.isDeleted() && revisionResponse.getReturnDate() == null) {
                EquipmentInResponseDto equipmentInResponseDto = equipmentInServiceBO.findByLastProviderAccountAndEquipmentId(providerAccountId, reversalRequest.getEquipmentId());
                if (equipmentInResponseDto.isFinish()) {
                    updateEquipmentAndReversal(providerAccountId, reversalRequest, equipmentInResponseDto);
                }

                List<EquipmentOutResponseDto> equipmentOutList = equipmentOutServiceBO.findAllByCompletedServiceId(completedResponseDtoDefault.getId());
                processEquipmentOutReversal(equipmentOutList, Math.toIntExact(reversalRequest.getQuantity()));

                processReversal(reversalRequest, revisionResponse, transactionResponse, equipmentInResponseDto.getAmount(), completedResponseDtoDefault);
            }
        }
    }

    private void adjustTransactionAmounts(TransactionResponse transactionResponse, BigDecimal amount, ReversalCompletedServiceRequest reversalRequest) {
        //350
        BigDecimal oldWorkmanshipAmount = transactionResponse.getWorkmanshipAmount();
        //1910
        BigDecimal oldAmount = transactionResponse.getAmount();
        //1560
        BigDecimal oldServiceAmount = oldAmount.subtract(oldWorkmanshipAmount);

        //780
        BigDecimal newAmount = amount.multiply(new BigDecimal(reversalRequest.getQuantity()));

        //oldServiceAmount - ewAmount = 780
        BigDecimal newServiceValue = oldServiceAmount.subtract(newAmount);

        //350 - 150 - 200
        BigDecimal finalAmount = newServiceValue.add(oldWorkmanshipAmount.subtract(reversalRequest.getWorkmanshipAmount()));

        //200  + 780
        BigDecimal finalWorkmanshipAmount = oldWorkmanshipAmount.subtract(reversalRequest.getWorkmanshipAmount());

        transactionResponse.setWorkmanshipAmount(finalWorkmanshipAmount);

        transactionResponse.setAmount(finalAmount);

        // Salva ou processa a reversão com os valores ajustados
        savingReversal(transactionResponse, transactionResponse.getWorkmanshipAmount());
    }



    private void savingReversal(TransactionResponse transactionResponse, BigDecimal newWorkmanshipAmount) {
        log.info("Service: reversal transaction by transactionId: {}", transactionResponse.getId());
        transactionServiceBO.reversal(transactionResponse.getAmount(), newWorkmanshipAmount, transactionResponse.getId());
    }

    private void processReversal(ReversalCompletedServiceRequest reversalRequest, RevisionResponse revisionResponse, TransactionResponse transactionResponse, BigDecimal amount, CompletedResponseDtoDefault completedResponseDtoDefault) {

        String[] parts = transactionResponse.getCompletedServiceIds().split(",");
        if (parts.length > 1) {
            adjustTransactionAmounts(transactionResponse, amount, reversalRequest);
            revisionServiceBO.partialReversal(revisionResponse.getId(), partialReversalValue(reversalRequest, completedResponseDtoDefault));
            completedServiceRepository.partialReversal(completedResponseDtoDefault.getId(), partialReversalValue(reversalRequest, completedResponseDtoDefault));
        } else {
            if (reversalRequest.getQuantity().equals(completedResponseDtoDefault.getQuantity())) {
                log.info("Service: Reversal revision by id: {}", revisionResponse.getId());
                revisionServiceBO.reversal(revisionResponse.getId());
                transactionResponse.setAmount(BigDecimal.ZERO);
                savingReversal(transactionResponse, BigDecimal.ZERO);
            } else {
                adjustTransactionAmounts(transactionResponse, amount, reversalRequest);
                revisionServiceBO.partialReversal(revisionResponse.getId(), partialReversalValue(reversalRequest, completedResponseDtoDefault));
                completedServiceRepository.partialReversal(completedResponseDtoDefault.getId(), partialReversalValue(reversalRequest, completedResponseDtoDefault));
            }
        }
    }

    private static Long partialReversalValue(ReversalCompletedServiceRequest reversalRequest, CompletedResponseDtoDefault completedResponseDtoDefault) {
        return completedResponseDtoDefault.getQuantity() - reversalRequest.getQuantity();
    }

    private boolean isValidTransaction(CompletedResponseDtoDefault completedResponseDtoDefault, ReversalCompletedServiceRequest reversalRequest, Long providerAccountId) {
        log.info("Checks sameProvider {}.", completedResponseDtoDefault.getProviderAccountId());
        boolean isSameProvider = completedResponseDtoDefault.getProviderAccountId().equals(providerAccountId);

        log.info("Checks if the requested quantity is less than or equal to the available quantity by transactionId: {}.", completedResponseDtoDefault.getTransactionId());
        boolean isQuantityValid = reversalRequest.getQuantity() <= completedResponseDtoDefault.getQuantity();

        log.info("Checks if the creation date is within the last 24 hours by transactionId: {}.", completedResponseDtoDefault.getTransactionId());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createDate = completedResponseDtoDefault.getCreateDate();
        boolean isWithin24Hours = Duration.between(createDate, now).toHours() <= 24;

        return isSameProvider && isQuantityValid && isWithin24Hours;
    }

    private void updateEquipmentAndReversal(Long providerAccountId, ReversalCompletedServiceRequest reversalRequest, EquipmentInResponseDto equipmentInResponseDto) {
        log.info("saving equipment quantities and amounts for providerAccountId: {}, equipmentId: {}", providerAccountId, equipmentInResponseDto.getId());
        EquipmentInRequest equipmentRequest = new EquipmentInRequest();
        equipmentRequest.setQuantity(reversalRequest.getQuantity());
        equipmentRequest.setAmount(equipmentInResponseDto.getAmount());
        equipmentRequest.setProviderAccountId(providerAccountId);
        equipmentRequest.setEquipmentId(equipmentInResponseDto.getEquipmentId());

        equipmentInServiceBO.save(equipmentRequest);

        log.info("Equipment save successfully for providerAccountId: {}, equipmentId: {}", providerAccountId, equipmentInResponseDto.getId());
    }


    private void processEquipmentOutReversal(List<EquipmentOutResponseDto> equipmentOutList, int quantity) throws EquipmentException {
        if (equipmentOutList.size() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                log.info("Service: Reversal equipmentOut by id: {}", equipmentOutList.get(i).getId());
                equipmentOutServiceBO.reversal(equipmentOutList.get(i).getId());
            }
        } else {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "Not enough equipment available for reversal.");
        }
    }
}