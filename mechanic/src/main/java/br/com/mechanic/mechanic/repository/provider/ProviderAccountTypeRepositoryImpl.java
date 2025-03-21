package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderAccountType;

import java.util.Optional;

public interface ProviderAccountTypeRepositoryImpl {
    ProviderAccountType save(ProviderAccountType providerPhone);

    Optional<ProviderAccountType> getProviderType(Long type);
}
