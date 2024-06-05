package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProviderServiceRepositoryJpa implements ProviderServiceRepositoryImpl {

    private final ProviderServiceRepository repository;

    public ProviderServiceRepositoryJpa(ProviderServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProviderService save(ProviderService entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
            entity.setIsEnabled(true);
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ProviderService> findByIdentifier(String identifier) {
        return Optional.empty();
    }

    @Override
    public Page<ProviderService> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ProviderService> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ProviderService> findByVehicleTypeId(Long vehicleTypeId) {
        return repository.findByVehicleTypeId(vehicleTypeId);
    }

    @Override
    public Optional<ProviderService> providerServiceIsExistsByAccountId(Long identifierId, Long providerAccountId, Long vehicleTypeId) {
        return repository.findByIdentifierIdAndProviderAccountIdAndVehicleTypeIdAndIsEnabledIsTrue(identifierId, providerAccountId,vehicleTypeId);
    }
}