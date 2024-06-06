package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleTypeServiceBO {
    VehicleTypeResponseDto findById(Long id);

    Page<VehicleTypeResponseDto> findAll(final Pageable pageable);

    VehicleTypeResponseDto save(VehicleTypeRequestDto vehicleTypeRequestDto) throws VehicleTypeException;

    VehicleTypeResponseDto updateVehicleTypeName(Long id, VehicleTypeRequestDto vehicleTypeRequestDto);
    void isUsed(Long id, Boolean isUsed);
}