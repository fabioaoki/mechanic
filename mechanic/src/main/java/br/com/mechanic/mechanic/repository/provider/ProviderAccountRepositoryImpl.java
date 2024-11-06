package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderAccountRepositoryImpl {

    Page<ProviderAccount> findAll(Pageable pageable);

    Optional<ProviderAccount> findById(Long id);

    Optional<ProviderAccount> findByCnpj(String cnpj);
    Optional<ProviderAccount> findByLogin(String login);

    ProviderAccount save(ProviderAccount entity);
}
