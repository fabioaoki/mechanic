package br.com.mechanic.mechanic.repository.pack;

import br.com.mechanic.mechanic.entity.pack.PackService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackServiceRepository extends JpaRepository<PackService, Long> {}
