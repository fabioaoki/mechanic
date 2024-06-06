package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Marc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarcRepository extends JpaRepository<Marc, Long> {

    Page<Marc> findAllByNameAndYear(Pageable pageable, String name, String year);

    Optional<Marc> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year);

    Optional<Marc> findByNameAndYear(String name, String year);
}
