package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import br.com.mechanic.mechanic.response.ProviderEquipmentInResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EquipmentInRepositoryImpl {
    Optional<EquipmentIn> findByProviderAccountId(final Long providerAccountId);

    EquipmentIn save(final EquipmentIn typeService);

    Page<EquipmentIn> findAll(final Pageable pageable);

    Page<ProviderEquipmentInResponseDto> findAllByProviderAccountId(final Pageable pageable, final Long providerAccountId);

    Optional<EquipmentIn> findById(final Long id);

    Optional<EquipmentIn> findByProviderAccountIdAndEquipmentId(final Long providerAccountId, final Long equipmentId);

    Optional<EquipmentIn> findByProviderAccountIdAndEquipmentIdAndFinishIsFalse(final Long providerAccountId, final Long equipmentId);
}
