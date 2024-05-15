package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.CompletedServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedServicesRepository extends JpaRepository<CompletedServices, Long> {}

