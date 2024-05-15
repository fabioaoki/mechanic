package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {}
