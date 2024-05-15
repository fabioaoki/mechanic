package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderRateResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRateResponseRepository extends JpaRepository<ProviderRateResponse, Long> {}
