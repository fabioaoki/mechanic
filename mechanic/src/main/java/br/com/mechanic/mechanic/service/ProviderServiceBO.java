package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.ProviderServiceResponseDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderServiceBO {
    ProviderServiceResponseDto findById(Long id);

    Page<ProviderServiceResponseDto> findAll(final Pageable pageable);

    ProviderServiceResponseDto save(ProviderServiceRequest providerServiceRequest) throws VehicleTypeException;

    ProviderServiceResponseDto updateVehicleTypeName(Long id, ProviderServiceRequest providerServiceRequest);
    void isUsed(Long id, boolean isUsed);

    void findByVehicleTypeId(Long vehicleTypeId);
}