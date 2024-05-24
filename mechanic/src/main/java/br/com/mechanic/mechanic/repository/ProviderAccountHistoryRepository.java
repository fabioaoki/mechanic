package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderAccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderAccountHistoryRepository extends JpaRepository<ProviderAccountHistory, Long> {
    List<Optional<ProviderAccountHistory>> findByProviderAccountId(Long providerAccountId);
}
