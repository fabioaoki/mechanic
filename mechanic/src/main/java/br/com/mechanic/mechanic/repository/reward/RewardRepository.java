package br.com.mechanic.mechanic.repository.reward;

import br.com.mechanic.mechanic.entity.reward.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {}
