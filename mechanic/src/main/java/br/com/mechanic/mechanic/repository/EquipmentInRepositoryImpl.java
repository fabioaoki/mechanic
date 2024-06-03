package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EquipmentInRepositoryImpl {
    Optional<EquipmentIn> findByProviderAccountId(final Long providerAccountId);

    EquipmentIn save(final EquipmentIn typeService);

    Page<EquipmentIn> findAll(final Pageable pageable);

    Page<EquipmentIn> findAllByProviderAccountId(final Pageable pageable, final Long providerAccountId);

    Optional<EquipmentIn> findById(final Long id);

    Optional<EquipmentIn> findByProviderAccountIdAndEquipmentId(final Long providerAccountId, final Long equipmentId);
}
