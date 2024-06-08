package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ModelRepositoryJpa implements ModelRepositoryImpl {

    MarcRepository repository;

    public ModelRepositoryJpa(MarcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Model save(Model entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Page<Model> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Model> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Model> findByNameAndYear(String name, String year) {
        return repository.findByNameAndYear(name, year);
    }

    @Override
    public Optional<Model> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year) {
        return repository.findByNameAndModelAndVersionAndYear(name, model, version, year);
    }

    @Override
    public Page<Model> findAllByModelAndYear(Pageable pageable, String model, String year) {
        return repository.findAllByModelAndYear(pageable, model, year);
    }

    @Override
    public Page<Model> findAllMarcByMarcAndYear(Pageable pageable, String marc, String year) {
        return repository.findAllByNameAndYear(pageable, marc, year);
    }

}
