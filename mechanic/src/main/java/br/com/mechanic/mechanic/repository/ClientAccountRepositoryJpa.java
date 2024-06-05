package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ClientAccount;
import br.com.mechanic.mechanic.entity.EmployeeAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ClientAccountRepositoryJpa implements ClientAccountRepositoryImpl {

    private final ClientAccountRepository repository;

    @Autowired
    public ClientAccountRepositoryJpa(ClientAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ClientAccount> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ClientAccount> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ClientAccount save(ClientAccount entity) {
        if(entity.getCreateDate() == null){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Optional<ClientAccount> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    @Override
    public Optional<ClientAccount> findByCpfOrRg(String cpf, String rg) {
        return repository.findByCpfOrRg(cpf, rg);
    }

    @Override
    public Optional<ClientAccount> findByRg(String rg) {
        return repository.findByRg(rg);
    }

}