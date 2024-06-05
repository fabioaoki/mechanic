package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderAccountHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ProviderAccountHistoryRepositoryJpa implements ProviderAccountHistoryRepositoryImpl {

    private final ProviderAccountHistoryRepository providerAccountHistoryRepository;

    @Autowired
    public ProviderAccountHistoryRepositoryJpa(ProviderAccountHistoryRepository providerAccountHistoryRepository) {
        this.providerAccountHistoryRepository = providerAccountHistoryRepository;
    }

    @Override
    public Page<ProviderAccountHistory> findAll(Pageable pageable) {
        return providerAccountHistoryRepository.findAll(pageable);
    }

    @Override
    public Optional<ProviderAccountHistory> findById(Long id) {
        return providerAccountHistoryRepository.findById(id);
    }

    @Override
    public List<Optional<ProviderAccountHistory>> findByProviderAccountId(Long providerAccountId) {
        return providerAccountHistoryRepository.findByProviderAccountId(providerAccountId);
    }

    @Override
    public ProviderAccountHistory save(ProviderAccountHistory entity) {
        entity.setCreateDate(LocalDateTime.now());
        return providerAccountHistoryRepository.save(entity);
    }
}
