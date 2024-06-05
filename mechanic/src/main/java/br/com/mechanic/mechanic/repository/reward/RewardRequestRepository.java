package br.com.mechanic.mechanic.repository.reward;

import br.com.mechanic.mechanic.entity.reward.RewardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRequestRepository extends JpaRepository<RewardRequest, Long> {}

