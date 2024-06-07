package br.com.mechanic.mechanic.service.vehicle;

import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.SaveVehicleRequest;
import br.com.mechanic.mechanic.response.VehicleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleServiceBO {
    VehicleResponseDto findById(final Long id);

    Page<VehicleResponseDto> findAll(final Pageable pageable);

    Page<VehicleResponseDto> findAllByClientAccountId(final Pageable pageable, Long clientAccountId);

    VehicleResponseDto save(final SaveVehicleRequest vehicleTypeRequestDto, boolean viaClientController) throws VehicleTypeException;

    void sold(final Long id, final boolean sold);
}