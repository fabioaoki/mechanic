package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class VehicleTypeRepositoryJpa implements VehicleTypeRepositoryImpl {

    VehicleTypeRepository repository;

    public VehicleTypeRepositoryJpa(VehicleTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public VehicleType save(VehicleType entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
            entity.setUse(true);
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<VehicleType> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<VehicleType> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<VehicleType> findByName(String name) {
        return repository.findByName(name);
    }
}
