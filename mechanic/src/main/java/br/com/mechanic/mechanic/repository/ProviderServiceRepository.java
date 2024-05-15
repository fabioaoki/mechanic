package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderServiceRepository extends JpaRepository<ProviderService, Long> {}
