package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.ProviderServiceException;
import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.service.request.ProviderServiceRequest;
import br.com.mechanic.mechanic.service.request.ProviderServiceUpdateRequestDto;
import br.com.mechanic.mechanic.service.response.ProviderServiceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProviderServiceBO {
    ProviderServiceResponseDto findById(Long id);

    Page<ProviderServiceResponseDto> findAll(final Pageable pageable);

    List<ProviderServiceResponseDto> save(ProviderServiceRequest providerServiceRequest) throws ProviderServiceException, VehicleTypeException, ProviderAccountException;

    ProviderServiceResponseDto updateProviderServiceIdentifier(Long id, ProviderServiceUpdateRequestDto providerServiceRequest);

    void isUsed(Long id, boolean isUsed);

    void findByVehicleTypeId(Long vehicleTypeId);
}