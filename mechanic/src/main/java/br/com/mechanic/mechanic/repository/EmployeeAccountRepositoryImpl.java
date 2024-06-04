package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.EmployeeAccount;
import br.com.mechanic.mechanic.entity.ProviderPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeAccountRepositoryImpl {

    Page<EmployeeAccount> findAll(Pageable pageable);

    Optional<EmployeeAccount> findById(Long id);

    EmployeeAccount save(EmployeeAccount entity);
}
