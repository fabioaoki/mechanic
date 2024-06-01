package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderServiceIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderServiceIdentifierRepository extends JpaRepository<ProviderServiceIdentifier, Long> {
    Optional<ProviderServiceIdentifier> findByIdentifier(String identifier);
}
