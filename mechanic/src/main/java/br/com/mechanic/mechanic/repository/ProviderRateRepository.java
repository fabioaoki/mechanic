package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRateRepository extends JpaRepository<ProviderRate, Long> {}
