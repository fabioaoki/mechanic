package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ClientPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientPersonRepository extends JpaRepository<ClientPerson, Long> {}

