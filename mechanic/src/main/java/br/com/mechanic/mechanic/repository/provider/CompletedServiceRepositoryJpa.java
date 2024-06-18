package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class CompletedServiceRepositoryJpa implements CompletedServiceRepositoryImpl {

    CompletedServiceRepository repository;

    public CompletedServiceRepositoryJpa(CompletedServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompletedService save(CompletedService entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<CompletedService> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<CompletedService> findById(Long id) {
        return repository.findById(id);
    }

}