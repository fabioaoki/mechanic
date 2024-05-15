package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.request.ProviderAccountRequest;
import br.com.mechanic.mechanic.response.ProviderAccountResponse;

import java.util.List;
import java.util.Optional;

public interface ProviderAccountServiceBO {
    Optional<ProviderAccountResponse> findById(Long id);

    List<ProviderAccountResponse> findAll();

    ProviderAccountResponse save(ProviderAccountRequest providerAccount);

    void delete(ProviderAccountResponse providerAccountResponse);
}
