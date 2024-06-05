package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VehicleTypeRepositoryImpl {
    Optional<VehicleType> findByName(String name);

    VehicleType save(VehicleType vehicleType);

    Page<VehicleType> findAll(Pageable pageable);

    Optional<VehicleType> findById(Long id);
}
