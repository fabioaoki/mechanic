package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {
    Optional<ClientAccount> findByCpf(String cpf);

    Optional<ClientAccount> findByCpfOrRg(String cpf, String rg);

    Optional<ClientAccount> findByRg(String rg);
}
