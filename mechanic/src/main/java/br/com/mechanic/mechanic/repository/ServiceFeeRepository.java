package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ServiceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {}
