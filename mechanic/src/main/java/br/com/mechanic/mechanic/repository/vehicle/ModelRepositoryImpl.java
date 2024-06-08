package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ModelRepositoryImpl {
    Model save(Model plate);

    Page<Model> findAll(Pageable pageable);

    Optional<Model> findById(Long id);

    Optional<Model> findByNameAndYear(String name, String year);

    Optional<Model> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year);

    Page<Model> findAllByModelAndYear(Pageable pageable, String model, String year);

    Page<Model> findAllMarcByMarcAndYear(Pageable pageable, String marc, String year);
}
