package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAccountHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProviderAccountHistoryRepositoryImpl {

    Page<ProviderAccountHistory> findAll(Pageable pageable);

    Optional<ProviderAccountHistory> findById(Long id);

    List<Optional<ProviderAccountHistory>> findByProviderAccountId(Long providerAccountId);

    ProviderAccountHistory save(ProviderAccountHistory entity);
}
