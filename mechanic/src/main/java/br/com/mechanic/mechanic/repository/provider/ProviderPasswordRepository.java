package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.ProviderPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderPasswordRepository extends JpaRepository<ProviderPassword, Long> {
    Optional<ProviderPassword> findByProviderAccountId(Long providerAccountId);

    Optional<ProviderPassword> findByEmail(String email);
}
