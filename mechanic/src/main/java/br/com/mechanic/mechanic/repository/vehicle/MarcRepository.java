package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarcRepository extends JpaRepository<Model, Long> {


    Page<Model> findAllByModelAndYear(Pageable pageable, String model, String year);

    Optional<Model> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year);

    Optional<Model> findByNameAndYear(String name, String year);

    Page<Model> findAllByNameAndYear(Pageable pageable, String name, String year);
}
