package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardTypeRepository extends JpaRepository<RewardType, Long> {}
