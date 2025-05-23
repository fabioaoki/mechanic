package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedServicesRepository extends JpaRepository<CompletedService, Long> {}

