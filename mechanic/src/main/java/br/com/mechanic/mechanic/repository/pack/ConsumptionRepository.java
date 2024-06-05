package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.reward.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {}

