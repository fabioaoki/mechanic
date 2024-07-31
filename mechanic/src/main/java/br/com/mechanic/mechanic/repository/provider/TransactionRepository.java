package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    Page<Transaction> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.amount = :totalAmount, t.workmanshipAmount = :workmanshipAmount, t.lastUpdate = :now WHERE t.id = :id")
    void reversal(@Param("id") Long id, @Param("totalAmount") BigDecimal totalAmount, @Param("workmanshipAmount") BigDecimal workmanshipAmount, @Param("now") LocalDateTime now);
}