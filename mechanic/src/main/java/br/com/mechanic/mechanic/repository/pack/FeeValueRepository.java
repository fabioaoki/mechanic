package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.reward.FeeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeValueRepository extends JpaRepository<FeeValue, Long> {}
