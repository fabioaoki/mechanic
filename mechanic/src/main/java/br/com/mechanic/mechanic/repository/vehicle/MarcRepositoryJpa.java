package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Marc;
import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.request.FindMarcAndYearRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class MarcRepositoryJpa implements MarcRepositoryImpl {

    MarcRepository repository;

    public MarcRepositoryJpa(MarcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Marc save(Marc entity) {
        entity.setCreateDate(LocalDateTime.now());
        return repository.save(entity);
    }

    @Override
    public Page<Marc> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Marc> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Marc> findByNameAndYear(String name, String year) {
        return repository.findByNameAndYear(name, year);
    }

    @Override
    public Optional<Marc> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year) {
        return repository.findByNameAndModelAndVersionAndYear(name, model, version, year);
    }

    @Override
    public Page<Marc> findAllByNameAndYear(Pageable pageable, String name, String year) {
        return repository.findAllByNameAndYear(pageable, name, year);
    }

}
