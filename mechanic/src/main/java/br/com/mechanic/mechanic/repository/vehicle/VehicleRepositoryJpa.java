
package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class VehicleRepositoryJpa implements VehicleRepositoryImpl {

    VehicleRepository repository;

    public VehicleRepositoryJpa(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Vehicle save(Vehicle entity) {
        if (Objects.isNull(entity.getCreateDate())) {
            entity.setCreateDate(LocalDateTime.now());
            entity.setSold(false);
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<Vehicle> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Vehicle> vehicleIsExists(Long plateId) {
        return repository.findByPlateId(plateId);
    }

    @Override
    public Page<Vehicle> findAllByClientAccountId(Pageable pageable, Long clientAccountId) {
        return repository.findAllByClientAccountId(pageable, clientAccountId);
    }
}