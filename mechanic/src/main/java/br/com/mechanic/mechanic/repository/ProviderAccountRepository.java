package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
    Optional<ProviderAccount> findByCnpj(String cnpj);
}
