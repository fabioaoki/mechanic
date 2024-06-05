package br.com.mechanic.mechanic.repository.employee;

import br.com.mechanic.mechanic.entity.provider.emloyee.EmployeeAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EmployeeAccountRepositoryJpa implements EmployeeAccountRepositoryImpl {

    private final EmployeeAccountRepository repository;

    @Autowired
    public EmployeeAccountRepositoryJpa(EmployeeAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<EmployeeAccount> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<EmployeeAccount> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public EmployeeAccount save(EmployeeAccount entity) {
        if(entity.getCreateDate() == null){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<EmployeeAccount> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

}