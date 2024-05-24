package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPhone;

import java.util.Optional;

public interface ProviderPhoneRepositoryImpl {
    ProviderPhone save(ProviderPhone providerPhone);

    Optional<ProviderPhone> findByPhone(String number);
}
