package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.PlateException;
import br.com.mechanic.mechanic.exception.ProviderAddressException;
import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.mapper.PlateMapper;
import br.com.mechanic.mechanic.model.PlateModel;
import br.com.mechanic.mechanic.model.VehicleTypeModel;
import br.com.mechanic.mechanic.repository.vehicle.PlateRepositoryImpl;
import br.com.mechanic.mechanic.request.PlateRequest;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.PlateResponseDto;
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
public class PlateService implements PlateServiceBO {

    private final PlateRepositoryImpl plateRepository;

    private final CityUtil cityUtil;

    @Override
    public PlateResponseDto save(PlateRequest requestDto, Long clientAccountId) {
        log.info("Service: valid plate field");
        validPlateField(requestDto);
        log.info("Service: Saving a new plate");
        PlateModel plateModel = PlateMapper.MAPPER.dtoToModel(requestDto);
        plateModel.setClientAccountId(clientAccountId);
        Plate plate = PlateMapper.MAPPER.toEntity(requestDto);
        return PlateMapper.MAPPER.toDto(plateRepository.save(plate));
    }

    @Override
    public Page<VehicleTypeResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of vehicle types");
        return plateRepository.findAll(pageable).map(PlateMapper.MAPPER::toDto);
    }

    @Override
    public VehicleTypeResponseDto findById(Long id) {
        return PlateMapper.MAPPER.toDto(getVehicleType(id));
    }

    public VehicleTypeResponseDto updateVehicleTypeName(Long id, VehicleTypeRequestDto requestDto) {
        log.info("Service update vehicle type by id: {}", id);
        VehicleTypeModel vehicleTypeModel = PlateMapper.MAPPER.toModel(getVehicleType(id));
        boolean isChange = updateField(vehicleTypeModel, requestDto);
        if (isChange) {
            PlateIsExists(vehicleTypeModel.getName());
            VehicleType vehicleType = plateRepository.save(PlateMapper.MAPPER.modelToEntity(vehicleTypeModel));
            return PlateMapper.MAPPER.toDto(vehicleType);
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
        plateRepository.save(vehicleType);
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
        return plateRepository.findById(id).orElseThrow(() -> new VehicleTypeException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Vehicle type not found by id: " + id));
    }

    private void validPlateField(PlateRequest requestDto) {

        if ((requestDto.getMercosulPlate() == null || requestDto.getMercosulPlate().isEmpty()) &&
                (requestDto.getOldPlate() == null || requestDto.getOldPlate().isEmpty())) {
            throw new PlateException(ErrorCode.INVALID_FIELD, "At least one of 'mercosulPlate' or 'oldPlate' must be filled.");
        }

        if ((requestDto.getMercosulPlate() != null && !requestDto.getMercosulPlate().isEmpty()) &&
                (requestDto.getOldPlate() != null && !requestDto.getOldPlate().isEmpty())) {
            throw new PlateException(ErrorCode.INVALID_FIELD, "Only one of 'mercosulPlate' or 'oldPlate' should be filled, not both.");
        }

        if (requestDto.getMercosulPlate() != null && !requestDto.getMercosulPlate().isEmpty()) {
            if (!requestDto.getMercosulPlate().matches("^[A-Z]{3}\\d[A-Z]\\d{2}$")) {
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'mercosulPlate' field is invalid. It should be in the format 'ABC1D23'.");
            }
            mercosulPlateIsExists(requestDto.getMercosulPlate());
        }

        if (requestDto.getOldPlate() != null && !requestDto.getOldPlate().isEmpty()) {
            if (!requestDto.getOldPlate().matches("^[A-Z]{3}-\\d{4}$")) {
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'oldPlate' field is invalid. It should be in the format 'AAA-1234'.");
            }
            if (requestDto.getCity() == null || requestDto.getCity().isEmpty()) {
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'city' field is required when 'oldPlate' is filled.");
            }
            String formattedCity = cityUtil.formatCity(requestDto.getCity());
            if (!cityUtil.isValidCity(formattedCity)) {
                throw new PlateException(ErrorCode.INVALID_FIELD, "The 'city' field is invalid.");
            }
            oldPlateIsExists(requestDto.getOldPlate(), formattedCity);
        }
    }

    private void mercosulPlateIsExists(String mercosulPlate) {
        plateRepository.findByPlate(mercosulPlate)
                .ifPresent(number -> {
                    throw new PlateException(ErrorCode.ERROR_CREATED_PLATE, "Mercosul plate already registered");
                });
    }

    private void oldPlateIsExists(String oldPlate, String city) {
        plateRepository.findByOldPlateAndCity(oldPlate, city)
                .ifPresent(number -> {
                    throw new PlateException(ErrorCode.ERROR_CREATED_PLATE, "Old plate already registered in the specified city");
                });
    }
}