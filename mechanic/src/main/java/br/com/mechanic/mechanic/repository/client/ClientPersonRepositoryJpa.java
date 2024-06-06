package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ClientPersonRepositoryJpa implements ClientPersonRepositoryImpl {

    private final ClientPersonRepository repository;

    @Autowired
    public ClientPersonRepositoryJpa(ClientPersonRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<ClientPerson> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<ClientPerson> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ClientPerson save(ClientPerson entity) {
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }
}