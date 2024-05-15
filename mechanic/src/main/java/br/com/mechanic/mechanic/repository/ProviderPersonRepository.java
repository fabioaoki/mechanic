package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.ProviderPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderPersonRepository extends JpaRepository<ProviderPerson, Long> {}
