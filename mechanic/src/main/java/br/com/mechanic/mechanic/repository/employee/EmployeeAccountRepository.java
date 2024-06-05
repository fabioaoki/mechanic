package br.com.mechanic.mechanic.repository.employee;

import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, Long> {
    Optional<EmployeeAccount> findByCpf(String cpf);
}

