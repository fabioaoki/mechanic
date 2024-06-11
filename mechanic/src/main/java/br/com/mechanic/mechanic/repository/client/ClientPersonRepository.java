package br.com.mechanic.mechanic.repository.client;

import br.com.mechanic.mechanic.entity.client.ClientPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPersonRepository extends JpaRepository<ClientPerson, Long> {
    Optional<ClientPerson> findByClientAccountId(Long id);
}

