package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {
    Optional<ProviderService> findByVehicleTypeId(Long vehicleTypeId);

    Optional<ProviderService> findByIdentifierIdAndProviderAccountIdAndVehicleTypeIdAndIsEnabledIsTrue(Long identifierId, Long providerAccountId, Long vehicleTypeId);
}
