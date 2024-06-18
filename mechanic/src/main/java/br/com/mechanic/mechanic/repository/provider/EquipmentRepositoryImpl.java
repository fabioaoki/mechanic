package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EquipmentRepositoryImpl {
    Optional<Equipment> findByName(String name);

    Equipment save(Equipment typeService);

    Page<Equipment> findAll(Pageable pageable);

    Optional<Equipment> findById(Long id);

    Optional<Equipment> findByProviderServiceIdentifierId(Long providerServiceIdentifierId);
}
