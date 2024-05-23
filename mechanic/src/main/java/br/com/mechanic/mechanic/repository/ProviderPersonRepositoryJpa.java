package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProviderPersonRepositoryJpa implements ProviderPersonRepositoryImpl {

    private final ProviderPersonRepository repository;

    @Autowired
    public ProviderPersonRepositoryJpa(ProviderPersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ProviderPerson> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ProviderPerson> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public ProviderPerson save(ProviderPerson entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }
}
