package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.SaveVehicleRequest;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.VehicleResponseDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleServiceBO {
    VehicleResponseDto findById(Long id);

    Page<VehicleResponseDto> findAll(final Pageable pageable);

    VehicleResponseDto save(SaveVehicleRequest vehicleTypeRequestDto) throws VehicleTypeException;

    VehicleResponseDto updateVehicleTypeName(Long id, boolean sold);
}