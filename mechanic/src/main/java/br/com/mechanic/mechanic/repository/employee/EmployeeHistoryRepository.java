package br.com.mechanic.mechanic.repository.employee;

import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {}
