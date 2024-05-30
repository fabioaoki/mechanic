package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.ProviderService;
import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.mapper.ProviderServiceMapper;
import br.com.mechanic.mechanic.model.ProviderServiceModel;
import br.com.mechanic.mechanic.repository.ProviderServiceRepositoryImpl;
import br.com.mechanic.mechanic.repository.VehicleTypeRepositoryImpl;
import br.com.mechanic.mechanic.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
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
    private final VehicleTypeRepositoryImpl typeRepository;

    private final ProviderAccountServiceBO providerAccountServiceBO;
    private final TypeServiceBO typeServiceBO;

    @Override
    public ProviderServiceResponseDto save(ProviderServiceRequest requestDto) {
        log.info("Service: valid provider service field");
        validProviderServiceField(requestDto);
        log.info("Service: Saving a new provider service");
        ProviderServiceModel providerServiceModel = getProviderServiceModel(requestDto);
        ProviderService providerService = ProviderServiceMapper.MAPPER.modelToEntity(providerServiceModel);
        return ProviderServiceMapper.MAPPER.toDto(serviceRepository.save(providerService));
    }

    private ProviderServiceModel getProviderServiceModel(ProviderServiceRequest requestDto) {
        ProviderServiceModel providerServiceModel = ProviderServiceMapper.MAPPER.dtoToModel(requestDto);
        providerServiceModel.setName(providerServiceModel.getName());
        providerServiceModel.setName(capitalizeFirstLetter(providerServiceModel.getName().trim()));
        return providerServiceModel;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Override
    public ProviderServiceResponseDto updateVehicleTypeName(Long id, ProviderServiceRequest providerServiceRequest) {
        return null;
    }

    @Override
    public Page<ProviderServiceResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of provider services");
        return serviceRepository.findAll(pageable).map(ProviderServiceMapper.MAPPER::toDto);
    }

    @Override
    public ProviderServiceResponseDto findById(Long id) {
        return ProviderServiceMapper.MAPPER.toDto(getProviderService(id));
    }

    public ProviderServiceResponseDto updateProviderServiceName(Long id, ProviderServiceRequest requestDto) {
        log.info("Service update provider service by id: {}", id);
        ProviderServiceModel vehicleTypeModel = ProviderServiceMapper.MAPPER.toModel(getProviderService(id));
        boolean isChange = updateField(vehicleTypeModel, requestDto);
        if (isChange) {
            providerServiceIsExistsByAccountId(vehicleTypeModel.getIdentifier().toString(), vehicleTypeModel.getTypeServiceId(), vehicleTypeModel.getProviderAccountId(), vehicleTypeModel.getVehicleTypeId());
            ProviderService vehicleType = serviceRepository.save(ProviderServiceMapper.MAPPER.modelToEntity(vehicleTypeModel));
            return ProviderServiceMapper.MAPPER.toDto(vehicleType);
        }
        throw new ProviderAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the provider address.");
    }

    @Override
    public void isUsed(Long id, boolean isUsed) {
        ProviderService providerService = getProviderService(id);
        if (isUsed == false) {
            //verifica se algum servico usa
        }
        providerService.setIsEnabled(isUsed);
        serviceRepository.save(providerService);
    }

    @Override
    public void findByVehicleTypeId(Long vehicleTypeId) {
        providerServiceIsUsed(vehicleTypeId);
    }

    private boolean updateField(ProviderServiceModel phoneModel, ProviderServiceRequest requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getName()) && !Objects.equals(phoneModel.getName(), requestDto.getName())) {
            phoneModel.setName(requestDto.getName());
            isChange = true;
        }
        return isChange;
    }

    private ProviderService getProviderService(Long id) {
        return serviceRepository.findById(id).orElseThrow(() -> new ProviderServiceException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Vehicle type not found by id: " + id));
    }

    private void validProviderServiceField(ProviderServiceRequest requestDto) {
        if (Objects.isNull(requestDto.getProviderAccountId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'providerAccountId' field is required and cannot be empty.");
        }
        ProviderAccountResponseDto providerAccountId = providerAccountServiceBO.findById(requestDto.getProviderAccountId());
        if (providerAccountId.getStatus().equals(ProviderAccountStatusEnum.CANCEL)) {
            throw new ProviderAccountException(ErrorCode.ERROR_PROVIDER_ACCOUNT_STATUS_IS_CANCEL, "The 'providerAccountStatus' is canceled.");
        }
        if (Objects.isNull(requestDto.getTypeServiceId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'typeAccountId' field is required and cannot be empty.");
        }
        typeServiceBO.findById(requestDto.getTypeServiceId());

        if (Objects.isNull(requestDto.getVehicleTypeId())) {
            throw new ProviderServiceException(ErrorCode.INVALID_FIELD, "The 'vehicleTypeId' field is required and cannot be empty.");
        }
        typeRepository.findById(requestDto.getVehicleTypeId()).orElseThrow(() -> new TypeServiceException(ErrorCode.ERROR_VEHICLE_TYPE, "Vehicle type not found by id: " + requestDto.getVehicleTypeId()));
        if (Objects.isNull(requestDto.getIdentifier())) {
            throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'identifier' field is required and cannot be empty.");
        }
        if (Objects.isNull(requestDto.getName()) || requestDto.getName().trim().isEmpty()) {
            throw new ProviderAddressException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        providerServiceIsExistsByAccountId(requestDto.getIdentifier().toString(), requestDto.getTypeServiceId(), requestDto.getProviderAccountId(), requestDto.getVehicleTypeId());
    }

    private void providerServiceIsExistsByAccountId(String identifier, Long typeServiceId, Long providerAccountId, Long vehicleTypeId) {
        serviceRepository.providerServiceIsExistsByAccountId(identifier, typeServiceId, providerAccountId, vehicleTypeId)
                .ifPresent(providerService -> {
                    throw new ProviderServiceException(ErrorCode.ERROR_CREATED_PROVIDER_SERVICE, "Provider service already registered");
                });
    }

    private void providerServiceIsExists(String identifier) {
        serviceRepository.findByIdentifier(identifier)
                .ifPresent(providerService -> {
                    throw new ProviderServiceException(ErrorCode.ERROR_CREATED_PROVIDER_SERVICE, "Provider service already registered");
                });
    }

    private void providerServiceIsUsed(Long vehicleTypeId) {
        serviceRepository.findByVehicleTypeId(vehicleTypeId)
                .ifPresent(providerService -> {
                    throw new ProviderServiceException(ErrorCode.ERROR_PROVIDER_SERVICE_IN_USED, "Provider service in used");
                });
    }
}