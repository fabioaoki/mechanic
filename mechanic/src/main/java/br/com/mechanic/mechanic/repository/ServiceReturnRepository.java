package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ServiceReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceReturnRepository extends JpaRepository<ServiceReturn, Long> {}