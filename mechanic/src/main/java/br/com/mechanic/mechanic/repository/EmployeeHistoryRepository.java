package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {}
