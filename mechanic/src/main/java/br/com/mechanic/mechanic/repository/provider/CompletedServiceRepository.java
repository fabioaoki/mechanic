package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import br.com.mechanic.mechanic.entity.provider.EquipmentIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompletedServiceRepository extends JpaRepository<CompletedService, Long> {
    Optional<CompletedService> findByProviderAccountId(Long providerAccountId);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    @Modifying
    @Transactional
    @Query("UPDATE CompletedService cs SET cs.transactionId = :transactionId WHERE cs.id IN :completedServiceIds")
    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);
}

