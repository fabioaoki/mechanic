package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.QuoteServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class QuoteServiceRepositoryJpa implements QuoteServiceRepositoryImpl {

    private final QuoteServiceRepository repository;

    public QuoteServiceRepositoryJpa(QuoteServiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public QuoteServiceEntity save(QuoteServiceEntity entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Page<QuoteServiceEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<QuoteServiceEntity> findAllByProviderAccountId(Pageable pageable, Long providerAccountId) {
        return repository.findAllByProviderAccountId(pageable, providerAccountId);
    }

    @Override
    public Optional<QuoteServiceEntity> findById(Long id) {
        return repository.findById(id);
    }

}