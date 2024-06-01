package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderService;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.exception.ProviderServiceException;
import br.com.mechanic.mechanic.mapper.ProviderServiceMapper;
import br.com.mechanic.mechanic.model.ProviderServiceModel;
import br.com.mechanic.mechanic.repository.ProviderServiceRepositoryImpl;
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

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class ProviderServiceManager implements ProviderServiceBO {

    private final ProviderServiceRepositoryImpl serviceRepository;
    private final ProviderAccountServiceBO providerAccountServiceBO;
    private ProviderServiceIdentifierBO serviceIdentifierBO;

    @Override
    public ProviderServiceResponseDto save(ProviderServiceRequest requestDto) {
        log.info("Service: valid provider service field");
        ProviderServiceIdentifierResponseDto serviceResponse = validProviderServiceField(requestDto);
        log.info("Service: Saving a new provider service");
        ProviderServiceModel providerServiceModel = getProviderServiceModel(requestDto);
        ProviderService providerService = ProviderServiceMapper.MAPPER.modelToEntity(providerServiceModel);

        return ProviderServiceMapper.MAPPER.toDto(serviceRepository.save(providerService), serviceResponse);
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
    public ProviderServiceResponseDto updateVehicleTypeName(Long id, ProviderServiceUpdateRequestDto updateRequestDto) {
        log.info("Service update provider service by id: {}", id);
        ProviderServiceModel providerServiceModel = ProviderServiceMapper.MAPPER.toModel(getProviderService(id));
        boolean isChange = updateField(providerServiceModel, updateRequestDto);
        ProviderServiceIdentifierResponseDto identifierResponseDto = serviceIdentifierBO.findById(updateRequestDto.getIdentifierId());
        if (isChange) {
            providerServiceIsExistsByAccountId(providerServiceModel.getIdentifierId(), providerServiceModel.getProviderAccountId(), providerServiceModel.getVehicleTypeId());
            ProviderService providerService = serviceRepository.save(ProviderServiceMapper.MAPPER.modelToEntity(providerServiceModel));

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

    private boolean updateField(ProviderServiceModel phoneModel, ProviderServiceUpdateRequestDto requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getIdentifierId()) && !Objects.equals(phoneModel.getIdentifierId(), requestDto.getIdentifierId())) {
            phoneModel.setIdentifierId(requestDto.getIdentifierId());
            isChange = true;
        }
        return isChange;
    }

    private ProviderService getProviderService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new ProviderServiceException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Vehicle type not found by id: " + id));
    }

    private ProviderServiceIdentifierResponseDto validProviderServiceField(ProviderServiceRequest requestDto) {
        if (Objects.isNull(requestDto.getProviderAccountId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty.");
        }
        ProviderAccountResponseDto providerAccountId = providerAccountServiceBO.findById(requestDto.getProviderAccountId());
        if (providerAccountId.getStatus().equals(ProviderAccountStatusEnum.CANCEL)) {
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled.");
        }
        if (Objects.isNull(requestDto.getVehicleTypeId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'vehicleTypeId' field is required and cannot be empty.");
        }
        if (Objects.isNull(requestDto.getIdentifierId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'identifier' field is required and cannot be empty.");
        }
        providerServiceIsExistsByAccountId(requestDto.getIdentifierId(), requestDto.getProviderAccountId(), requestDto.getVehicleTypeId());
        return serviceIdentifierBO.findById(requestDto.getIdentifierId());
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