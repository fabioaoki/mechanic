package br.com.mechanic.mechanic.repository.employee;

import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeAccountRepositoryImpl {

    Page<EmployeeAccount> findAll(Pageable pageable);

    Optional<EmployeeAccount> findById(Long id);

    EmployeeAccount save(EmployeeAccount entity);

    Optional<EmployeeAccount> findByCpf(String cpf);

}
