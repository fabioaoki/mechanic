package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {}
