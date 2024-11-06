package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderPassword;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProviderPasswordRepositoryJpa implements ProviderPasswordRepositoryImpl {

    private final ProviderPasswordRepository repository;

    public ProviderPasswordRepositoryJpa(ProviderPasswordRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProviderPassword save(ProviderPassword entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ProviderPassword> findByProviderAccountId(Long providerAccountId) {
        return repository.findByProviderAccountId(providerAccountId);
    }

    @Override
    public Optional<ProviderPassword> findByLogin(String login) {
        return repository.findByLogin(login);
    }
}
