package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderAccountRepository extends JpaRepository<ProviderAccount, Long> {
}
