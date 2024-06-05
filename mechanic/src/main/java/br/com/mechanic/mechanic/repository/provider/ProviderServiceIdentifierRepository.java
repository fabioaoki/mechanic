package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderServiceIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderServiceIdentifierRepository extends JpaRepository<ProviderServiceIdentifier, Long> {
    Optional<ProviderServiceIdentifier> findByIdentifier(String identifier);
}
