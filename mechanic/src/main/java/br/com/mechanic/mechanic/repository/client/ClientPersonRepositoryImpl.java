package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ClientPersonRepositoryImpl {

    Page<ClientPerson> findAll(Pageable pageable);

    Optional<ClientPerson> findById(Long id);
    Optional<ClientPerson> findByClientAccountId(Long id);

    ClientPerson save(ClientPerson entity);
}
