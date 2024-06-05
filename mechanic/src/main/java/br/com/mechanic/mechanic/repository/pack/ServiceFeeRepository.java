package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.pack.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {}
