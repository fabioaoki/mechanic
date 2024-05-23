package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAccountType;

import java.util.Optional;

public interface ProviderAccountTypeRepositoryImpl {
    ProviderAccountType save(ProviderAccountType providerPhone);

    Optional<ProviderAccountType> getProviderType(Long type);
}
