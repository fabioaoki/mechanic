package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderServiceIdentifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProviderServiceIdentifierRepositoryImpl {

    Page<ProviderServiceIdentifier> findAll(Pageable pageable);

    Optional<ProviderServiceIdentifier> findById(Long id);

    Optional<ProviderServiceIdentifier> findByIdentifier(String identifier);
}
