package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Marc;
import br.com.mechanic.mechanic.request.FindMarcAndYearRequest;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface MarcRepositoryImpl {
    Marc save(Marc plate);

    Page<Marc> findAll(Pageable pageable);

    Optional<Marc> findById(Long id);

    Optional<Marc> findByNameAndYear(String name, String year);

    Optional<Marc> findByNameAndModelAndVersionAndYear(String name, String model, String version, String year);

    Page<Marc> findAllByNameAndYear(Pageable pageable, String name, String year);
}
