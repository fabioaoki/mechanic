package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EquipmentOutRepositoryImpl {
    Long countByProviderAccountIdAndEquipmentId(final Long providerAccountId, final Long equipmentId);

    EquipmentOut save(final EquipmentOut typeService);

    Page<EquipmentOut> findAll(final Pageable pageable);

    Page<EquipmentOut> findAllByProviderAccountId(final Pageable pageable, final Long providerAccountId);

    Optional<EquipmentOut> findById(final Long id);

    List<EquipmentOut> findByProviderAccountAndEquipmentId(Long providerAccountId, Long id, LocalDateTime createDate);

    void reversal(Long id);

    List<EquipmentOut> findAllByCompletedServiceId(Long completedServiceId);
}
