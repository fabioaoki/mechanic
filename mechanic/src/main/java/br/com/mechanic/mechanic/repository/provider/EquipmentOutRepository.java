package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EquipmentOutRepository extends JpaRepository<EquipmentOut, Long> {
    Long countByProviderAccountIdAndEquipmentIdAndReversalIsFalse(Long providerAccountId, Long equipmentId);

    Page<EquipmentOut> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    @Query(value = "SELECT * FROM mechanic.equipment_out WHERE provider_account_id = :providerAccountId AND equipment_id = :equipmentId AND create_date >= :createDate AND reversal IS null", nativeQuery = true)
    List<EquipmentOut> findByProviderAccountAndEquipmentIdNative(
            @Param("providerAccountId") Long providerAccountId,
            @Param("equipmentId") Long equipmentId,
            @Param("createDate") LocalDateTime createDate
    );
}
