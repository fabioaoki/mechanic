package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompletedServiceRepositoryImpl {
    Page<CompletedService> findAll(Pageable pageable);

    Optional<CompletedService> findById(Long id);

    CompletedService save(CompletedService entity);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId, LocalDate startDate, LocalDate endDate);

    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);
}
