package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByName(String name);

    Optional<Equipment> findByProviderServiceIdentifierId(Long providerServiceIdentifierId);
}

