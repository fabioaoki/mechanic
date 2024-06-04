package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EquipmentOutRepositoryImpl {
    Long countByProviderAccountIdAndEquipmentId(final Long providerAccountId, final Long equipmentId);

    EquipmentOut save(final EquipmentOut typeService);

    Page<EquipmentOut> findAll(final Pageable pageable);

    Page<EquipmentOut> findAllByProviderAccountId(final Pageable pageable, final Long providerAccountId);

    Optional<EquipmentOut> findById(final Long id);
}
