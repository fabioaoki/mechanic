package br.com.mechanic.mechanic.repository.reward;

import br.com.mechanic.mechanic.entity.reward.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {}