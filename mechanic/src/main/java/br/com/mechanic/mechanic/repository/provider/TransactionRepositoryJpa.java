package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class TransactionRepositoryJpa implements TransactionRepositoryImpl {

    private final TransactionRepository repository;

    public TransactionRepositoryJpa(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Transaction> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Transaction> findAllByClientAccountId(Pageable pageable, Long clientAccountId) {
        return repository.findAllByClientAccountId(pageable, clientAccountId);
    }

    @Override
    public void reversal(Long id, BigDecimal totalAmount, BigDecimal workmanshipAmount) {
        repository.reversal(id, totalAmount, workmanshipAmount, LocalDateTime.now());
    }

}