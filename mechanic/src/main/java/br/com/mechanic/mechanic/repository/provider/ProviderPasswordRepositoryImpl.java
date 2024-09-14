package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderPassword;

import java.util.Optional;

public interface ProviderPasswordRepositoryImpl {
    ProviderPassword save(ProviderPassword providerPassword);

    Optional<ProviderPassword> findByProviderAccountId(Long providerAccountId);

    Optional<ProviderPassword> findByLogin(String login);
}
