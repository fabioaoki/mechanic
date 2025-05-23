package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface TransactionRepositoryImpl {

    Transaction save(final Transaction transaction);

    Page<Transaction> findAll(final Pageable pageable);

    Page<Transaction> findAllByProviderAccountId(final Pageable pageable, final Long providerAccountId);

    Optional<Transaction> findById(final Long id);

    Page<Transaction> findAllByClientAccountId(final Pageable pageable, final Long clientAccountId);

    void reversal(final Long id, final BigDecimal totalAmount, final BigDecimal workmanshipAmount);
}
