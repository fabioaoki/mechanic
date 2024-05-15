package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.RewardRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRequestRepository extends JpaRepository<RewardRequest, Long> {}

