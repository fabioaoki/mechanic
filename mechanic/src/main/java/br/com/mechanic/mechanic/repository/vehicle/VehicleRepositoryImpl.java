package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Vehicle;
import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VehicleRepositoryImpl {

    Vehicle save(Vehicle vehicleType);

    Page<Vehicle> findAll(Pageable pageable);

    Optional<Vehicle> findById(Long id);

    Optional<Vehicle> vehicleIsExists(Long plateId);

    Page<Vehicle> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

}
