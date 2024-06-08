package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Vehicle;
import br.com.mechanic.mechanic.exception.EquipmentException;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.VehicleException;
import br.com.mechanic.mechanic.mapper.VehicleMapper;
import br.com.mechanic.mechanic.repository.client.ClientAccountRepositoryImpl;
import br.com.mechanic.mechanic.repository.vehicle.VehicleRepositoryImpl;
import br.com.mechanic.mechanic.request.SaveVehicleRequest;
import br.com.mechanic.mechanic.response.VehicleResponseDto;
import br.com.mechanic.mechanic.service.ColorServiceBO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Log4j2
@Service
public class VehicleService implements VehicleServiceBO {

    private final VehicleRepositoryImpl vehicleRepository;
    private final ClientAccountRepositoryImpl clientAccountRepository;
    private final PlateServiceBO plateServiceBO;
    private final MarcServiceBO marcServiceBO;
    private final ColorServiceBO colorServiceBO;


    @Override
    public VehicleResponseDto save(SaveVehicleRequest requestDto, boolean viaClientController) {
        log.info("Service: valid vehicle field");
        validVehicleField(requestDto);
        if(!viaClientController){
            clientAccountRepository.findById(requestDto.getClientAccountId());
            plateServiceBO.findById(requestDto.getPlateId());
            marcServiceBO.findById(requestDto.getMarcId());
            colorServiceBO.findById(requestDto.getColorId());
        }
        log.info("Service: Saving a new vehicle");
        Vehicle vehicle = VehicleMapper.MAPPER.toEntity(requestDto);

        return VehicleMapper.MAPPER.toDto(vehicleRepository.save(vehicle));
    }

    @Override
    public void sold(Long id, boolean sold) {

    }

    @Override
    public Page<VehicleResponseDto> findAll(Pageable pageable) {
        log.info("Retrieving list of vehicle types");
        return vehicleRepository.findAll(pageable).map(VehicleMapper.MAPPER::toDto);
    }

    @Override
    public Page<VehicleResponseDto> findAllByClientAccountId(Pageable pageable, Long clientAccountId) {
        log.info("Retrieving list of vehicles by clientAccountId");
        return vehicleRepository.findAllByClientAccountId(pageable, clientAccountId).map(VehicleMapper.MAPPER::toDto);
    }

    @Override
    public VehicleResponseDto findById(Long id) {
        return VehicleMapper.MAPPER.toDto(getVehicle(id));
    }

    private Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new VehicleException(ErrorCode.VEHICLE_TYPE_EXCEPTION, "Vehicle type not found by id: " + id));
    }

    private void validVehicleField(SaveVehicleRequest requestDto) {
        if (Objects.isNull(requestDto.getClientAccountId()) || requestDto.getClientAccountId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'clientAccountId' field is required and cannot be empty or zero.");
        }
        if (Objects.isNull(requestDto.getMarcId()) || requestDto.getMarcId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'marcId' field is required and cannot be empty or zero.");
        }
        if (Objects.isNull(requestDto.getPlateId()) || requestDto.getPlateId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'plateId' field is required and cannot be empty or zero.");
        }
        if (Objects.isNull(requestDto.getColorId()) || requestDto.getColorId() == 0) {
            throw new EquipmentException(ErrorCode.INVALID_FIELD, "The 'colorId' field is required and cannot be empty or zero.");
        }
        vehicleIsExists(requestDto.getPlateId());
    }

    private void vehicleIsExists(Long plateId) {
        vehicleRepository.vehicleIsExists(plateId)
                .ifPresent(number -> {
                    throw new VehicleException(ErrorCode.ERROR_CREATED_VEHICLE, "Vehicle already registered");
                });
    }
}