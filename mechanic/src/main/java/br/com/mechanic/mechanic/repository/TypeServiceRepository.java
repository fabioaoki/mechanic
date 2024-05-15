package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeServiceRepository extends JpaRepository<TypeService, Long> {}

