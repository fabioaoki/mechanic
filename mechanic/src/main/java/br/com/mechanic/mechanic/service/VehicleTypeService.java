package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.mapper.VehicleTypeMapper;
import br.com.mechanic.mechanic.model.VehicleTypeModel;
import br.com.mechanic.mechanic.repository.vehicle.VehicleTypeRepositoryImpl;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class VehicleTypeService implements VehicleTypeServiceBO {

    private final VehicleTypeRepositoryImpl vehicleTypeRepository;
    private final ProviderServiceBO providerServiceBO;

    @Override
    public VehicleTypeResponseDto save(VehicleTypeRequestDto requestDto) {
        log.info("Service: valid vehicle field");
        validVehicleTypeField(requestDto);
        log.info("Service: Saving a new vehicle type");
        VehicleType vehicleType = VehicleTypeMapper.MAPPER.toEntity(requestDto);
        return VehicleTypeMapper.MAPPER.toDto(vehicleTypeRepository.save(vehicleType));
    }

    @Override
    public Page<VehicleTypeResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of vehicle types");
        return vehicleTypeRepository.findAll(pageable).map(VehicleTypeMapper.MAPPER::toDto);
    }

    @Override
    public VehicleTypeResponseDto findById(Long id) {
        return VehicleTypeMapper.MAPPER.toDto(getVehicleType(id));
    }

    public VehicleTypeResponseDto updateVehicleTypeName(Long id, VehicleTypeRequestDto requestDto) {
        log.info("Service update vehicle type by id: {}", id);
        VehicleTypeModel vehicleTypeModel = VehicleTypeMapper.MAPPER.toModel(getVehicleType(id));
        boolean isChange = updateField(vehicleTypeModel, requestDto);
        if (isChange) {
            vehicleTypeIsExists(vehicleTypeModel.getName());
            VehicleType vehicleType = vehicleTypeRepository.save(VehicleTypeMapper.MAPPER.modelToEntity(vehicleTypeModel));
            return VehicleTypeMapper.MAPPER.toDto(vehicleType);
        }
        throw new ProviderAddressException(ErrorCode.IDENTICAL_FIELDS, "No changes were made to the vehicle type.");
    }

    @Override
    public void isUsed(Long id, Boolean isUsed) {
        VehicleType vehicleType = getVehicleType(id);
        if (!isUsed) {
            providerServiceBO.findByVehicleTypeId(id);
        }
        vehicleType.setUse(isUsed);
        vehicleTypeRepository.save(vehicleType);
    }

    private boolean updateField(VehicleTypeModel phoneModel, VehicleTypeRequestDto requestDto) {
        boolean isChange = false;
        if (Objects.nonNull(requestDto.getName()) && !Objects.equals(phoneModel.getName(), requestDto.getName())) {
            phoneModel.setName(requestDto.getName());
            isChange = true;
        }
        return isChange;
    }

    private VehicleType getVehicleType(Long id) {
        return vehicleTypeRepository.findById(id).orElseThrow(() -> new VehicleTypeException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Vehicle type not found by id: " + id));
    }

    private void validVehicleTypeField(VehicleTypeRequestDto requestDto) {
        if (requestDto.getName() == null || requestDto.getName().isEmpty()) {
            throw new VehicleTypeException(ErrorCode.INVALID_FIELD, "The 'name' field is required and cannot be empty.");
        }
        vehicleTypeIsExists(requestDto.getName());
    }

    private void vehicleTypeIsExists(String name) {
        vehicleTypeRepository.findByName(name)
                .ifPresent(number -> {
                    throw new VehicleTypeException(ErrorCode.ERROR_CREATED_VEHICLE_TYPE, "Vehicle type already registered");
                });
    }
}