package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.ProviderService;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderServiceException;
import br.com.mechanic.mechanic.mapper.ProviderServiceMapper;
import br.com.mechanic.mechanic.model.ProviderServiceModel;
import br.com.mechanic.mechanic.model.ProviderServiceModelToUpdate;
import br.com.mechanic.mechanic.repository.provider.ProviderServiceRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.request.ProviderServiceUpdateRequestDto;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
import br.com.mechanic.mechanic.response.ProviderServiceIdentifierResponseDto;
import br.com.mechanic.mechanic.response.ProviderServiceResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderServiceManager implements ProviderServiceBO {

    private final ProviderServiceRepositoryImpl serviceRepository;
    private final ProviderAccountServiceBO providerAccountServiceBO;
    private ProviderServiceIdentifierBO serviceIdentifierBO;

    @Transactional
    @Override
    public List<ProviderServiceResponseDto> save(ProviderServiceRequest requestDto) {
        log.info("Service: Starting validation of provider service fields");
        List<ProviderServiceIdentifierResponseDto> services = validProviderServiceField(requestDto);
        log.info("Service: Validation complete. Number of services to process: {}", services.size());
        log.info("Service: Preparing provider service model");
        ProviderServiceModel providerServiceModel = getProviderServiceModel(requestDto);
        log.info("Service: Provider service model prepared with provider account ID: {}", providerServiceModel.getProviderAccountId());

        List<ProviderServiceResponseDto> serviceResponseList = new ArrayList<>();
        services.forEach(service -> {
            log.info("Service: Processing service with identifier: {}", service.getIdentifier());
            providerServiceModel.getVehicleTypeIds().forEach(vehicle -> {
                log.info("Service: Processing vehicle type ID: {}", vehicle);
                ProviderService providerServiceSave = ProviderServiceMapper.MAPPER.prepareToSave(providerServiceModel.getProviderAccountId(), vehicle, service.getId());
                log.info("Service: Prepared provider service entity for saving: {}", providerServiceSave);
                ProviderService savedProviderService = serviceRepository.save(providerServiceSave);
                log.info("Service: Provider service saved with ID: {}", savedProviderService.getId());
                ProviderServiceResponseDto dto = ProviderServiceMapper.MAPPER.toDto(savedProviderService, service);
                log.info("Service: Mapped provider service to response DTO: {}", dto);
                serviceResponseList.add(dto);
                log.info("Service: Added response DTO to the list. Current list size: {}", serviceResponseList.size());
            });
        });
        log.info("Service: Completed saving provider services. Total services saved: {}", serviceResponseList.size());
        return serviceResponseList;
    }


    private ProviderServiceModel getProviderServiceModel(ProviderServiceRequest requestDto) {
        return ProviderServiceMapper.MAPPER.dtoToModel(requestDto);
    }

//    private String capitalizeFirstLetter(String str) {
//        if (str == null || str.isEmpty()) {
//            return str;
//        }
//        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
//    }

    @Override
    public Page<ProviderServiceResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of provider services");
        return serviceRepository.findAll(pageable)
                .map(providerService -> {
                    ProviderServiceIdentifierResponseDto serviceIdentifier = serviceIdentifierBO.findById(providerService.getIdentifierId());
                    return ProviderServiceMapper.MAPPER.toDto(providerService, serviceIdentifier);
                });
    }

    @Override
    public ProviderServiceResponseDto findById(Long id) {
        ProviderService providerService = getProviderService(id);
        return ProviderServiceMapper.MAPPER.toDto(providerService, serviceIdentifierBO.findById(providerService.getIdentifierId()));
    }

    @Override
    public ProviderServiceResponseDto updateProviderServiceIdentifier(Long id, ProviderServiceUpdateRequestDto updateRequestDto) {
        log.info("Service update provider service by id: {}", id);
        ProviderServiceModelToUpdate modelToUpdate = ProviderServiceMapper.MAPPER.toModelToUpdate(getProviderService(id));
        boolean isChange = updateField(modelToUpdate, updateRequestDto);
        ProviderServiceIdentifierResponseDto identifierResponseDto = serviceIdentifierBO.findById(updateRequestDto.getIdentifierId());
        if (isChange) {
            providerServiceIsExistsByAccountId(modelToUpdate.getIdentifierId(), modelToUpdate.getProviderAccountId(), modelToUpdate.getVehicleTypeId());
            ProviderService providerService = serviceRepository.save(ProviderServiceMapper.MAPPER.modelToEntity(modelToUpdate));

            return ProviderServiceMapper.MAPPER.toDto(providerService, identifierResponseDto);
        }
        throw new ProviderServiceException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider service.");
    }

    @Override
    public void isUsed(Long id, boolean isUsed) {
        ProviderService providerService = getProviderService(id);
        if (isUsed != providerService.getIsEnabled()) {
            providerService.setIsEnabled(isUsed);
            serviceRepository.save(providerService);
        }
    }

    @Override
    public void findByVehicleTypeId(Long vehicleTypeId) {
        providerServiceIsUsed(vehicleTypeId);
    }

    private boolean updateField(ProviderServiceModelToUpdate modelToUpdate, ProviderServiceUpdateRequestDto requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getIdentifierId()) && !Objects.equals(modelToUpdate.getIdentifierId(), requestDto.getIdentifierId())) {
            modelToUpdate.setIdentifierId(requestDto.getIdentifierId());
            isChange = true;
        }
        return isChange;
    }

    private ProviderService getProviderService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new ProviderServiceException(ErrorCode.TYPE_SERVICE_NOT_FOUND, "Provider service not found by id: " + id));
    }

    private List<ProviderServiceIdentifierResponseDto> validProviderServiceField(ProviderServiceRequest requestDto) {
        if (Objects.isNull(requestDto.getProviderAccountId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty.");
        }
        ProviderAccountResponseDto providerAccountId = providerAccountServiceBO.findById(requestDto.getProviderAccountId());
        if (providerAccountId.getStatus().equals(ProviderAccountStatusEnum.CANCEL)) {
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled.");
        }
        if (Objects.isNull(requestDto.getVehicleTypeIds()) ||
                requestDto.getVehicleTypeIds().isEmpty() ||
                requestDto.getVehicleTypeIds().stream().anyMatch(id -> id == null || id == 0)) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'vehicleTypeIds' field is required and cannot be empty or contain null/zero values.");
        }
        if (Objects.isNull(requestDto.getServiceIdentifierIds()) ||
                requestDto.getServiceIdentifierIds().isEmpty() ||
                requestDto.getServiceIdentifierIds().stream().anyMatch(id -> id == null || id == 0)) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'identifier' field is required and cannot be empty.");
        }
        List<ProviderServiceIdentifierResponseDto> identifierList = new ArrayList<>();
        requestDto.getVehicleTypeIds().forEach(vehicleTypeId -> {
            requestDto.getServiceIdentifierIds().forEach(identifierId -> {
                providerServiceIsExistsByAccountId(identifierId, requestDto.getProviderAccountId(), vehicleTypeId);
                identifierList.add(serviceIdentifierBO.findById(identifierId));
            });
        });
        return removeDuplicate(identifierList);
    }

    private static List<ProviderServiceIdentifierResponseDto> removeDuplicate(List<ProviderServiceIdentifierResponseDto> identifierList) {
        Set<ProviderServiceIdentifierResponseDto> set = new HashSet<>(identifierList);
        return new ArrayList<>(set);
    }

    private void providerServiceIsExistsByAccountId(Long identifierId, Long providerAccountId, Long vehicleTypeId) {
        serviceRepository.providerServiceIsExistsByAccountId(identifierId, providerAccountId, vehicleTypeId)
                .ifPresent(providerService -> {
                    throw new ProviderServiceException(ErrorCode.ERROR_CREATED_PROVIDER_SERVICE, "Provider service already registered");
                });
    }

    private ProviderService getIdentifier(String identifier) {
        return serviceRepository.findByIdentifier(identifier).orElseThrow(() -> new ProviderServiceException(ErrorCode.ERROR_CREATED_PROVIDER_SERVICE, "Provider service identifier not found: " + identifier));
    }

    private void providerServiceIsUsed(Long vehicleTypeId) {
        serviceRepository.findByVehicleTypeId(vehicleTypeId)
                .ifPresent(providerService -> {
                    throw new ProviderServiceException(ErrorCode.ERROR_PROVIDER_SERVICE_IN_USED, "Provider service in used");
                });
    }
}