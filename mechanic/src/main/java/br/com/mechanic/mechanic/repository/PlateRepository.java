package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlateRepository extends JpaRepository<Plate, Long> {}
