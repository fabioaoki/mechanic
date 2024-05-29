package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderServiceRepositoryImpl {
    Optional<ProviderService> findByIdentifier(String identifier);

    ProviderService save(ProviderService providerService);

    Page<ProviderService> findAll(Pageable pageable);

    Optional<ProviderService> findById(Long id);

    Optional<ProviderService> findByVehicleTypeId(Long vehicleTypeId);
}

