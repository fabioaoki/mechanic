package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Exemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemptionRepository extends JpaRepository<Exemption, Long> {}
