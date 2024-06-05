package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentOutRepository extends JpaRepository<EquipmentOut, Long> {
    Long countByProviderAccountIdAndEquipmentIdAndReversalIsFalse(Long providerAccountId, Long equipmentId);

    Page<EquipmentOut> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
}
