package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.reward.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {}
