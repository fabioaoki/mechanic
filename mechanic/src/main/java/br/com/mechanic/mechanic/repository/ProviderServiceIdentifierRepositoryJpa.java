package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderServiceIdentifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProviderServiceIdentifierRepositoryJpa implements ProviderServiceIdentifierRepositoryImpl {

    ProviderServiceIdentifierRepository repository;

    public ProviderServiceIdentifierRepositoryJpa(ProviderServiceIdentifierRepository repository) {
        this.repository = repository;
    }


    @Override
    public Page<ProviderServiceIdentifier> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ProviderServiceIdentifier> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ProviderServiceIdentifier> findByIdentifier(String identifier) {
        return repository.findByIdentifier(identifier);
    }
}
