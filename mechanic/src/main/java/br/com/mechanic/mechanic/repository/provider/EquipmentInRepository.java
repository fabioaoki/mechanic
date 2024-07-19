package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentInRepository extends JpaRepository<EquipmentIn, Long> {
    Optional<EquipmentIn> findByProviderAccountId(Long providerAccountId);

    Optional<EquipmentIn> findByProviderAccountIdAndEquipmentIdAndFinishIsFalse(Long providerAccountId, Long equipmentId);

    @Query(value = "SELECT x.id, x.provider_account_id ,e.name ,x.create_date ,x.last_update ,x.finish , x.quantity , x.amount  " +
            "FROM mechanic.equipment_in x " +
            "INNER JOIN mechanic.equipment e ON x.equipment_id = e.id " +
            "WHERE x.provider_account_id = ?1 and x.finish = false",
            countQuery = "SELECT count(*) FROM mechanic.equipment_in x WHERE x.provider_account_id = ?1 and x.finish = false",
            nativeQuery = true)
    Page<Object[]> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);
}