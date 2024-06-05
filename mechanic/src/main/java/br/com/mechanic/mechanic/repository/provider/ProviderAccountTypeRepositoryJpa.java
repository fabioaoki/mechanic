package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.ProviderAccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProviderAccountTypeRepositoryJpa implements ProviderAccountTypeRepositoryImpl {

    private final ProviderAccountTypeRepository repository;

    @Autowired
    public ProviderAccountTypeRepositoryJpa(ProviderAccountTypeRepository providerAccountTypeRepository) {
        this.repository = providerAccountTypeRepository;
    }

    @Override
    public ProviderAccountType save(ProviderAccountType entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<ProviderAccountType> getProviderType(Long id) {
        return repository.findById(id);
    }
}
