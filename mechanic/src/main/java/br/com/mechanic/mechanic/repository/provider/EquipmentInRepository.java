package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentInRepository extends JpaRepository<EquipmentIn, Long> {
    Optional<EquipmentIn> findByProviderAccountId(Long providerAccountId);

    Optional<EquipmentIn> findByProviderAccountIdAndEquipmentIdAndFinishIsFalse(Long providerAccountId, Long equipmentId);

    Page<EquipmentIn> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
}

