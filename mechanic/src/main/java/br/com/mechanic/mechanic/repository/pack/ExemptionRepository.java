package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.reward.Exemption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExemptionRepository extends JpaRepository<Exemption, Long> {}
