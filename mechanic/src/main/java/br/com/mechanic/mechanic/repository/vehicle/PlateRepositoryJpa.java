package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import br.com.mechanic.mechanic.entity.vehicle.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class PlateRepositoryJpa implements PlateRepositoryImpl {

    PlateRepository repository;

    public PlateRepositoryJpa(PlateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Plate> findByMercosulPlate(String mercosulPlate) {
        return repository.findByMercosulPlate(mercosulPlate);
    }

    @Override
    public Plate save(Plate entity) {
        if(Objects.isNull(entity.getCreateDate())){
            entity.setCreateDate(LocalDateTime.now());
        } else {
            entity.setLastUpdate(LocalDateTime.now());
        }
        return repository.save(entity);
    }

    @Override
    public Page<Plate> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Plate> findAllByClientAccountId(Pageable pageable, Long clientAccountId) {
        return repository.findAllByClientAccountId(pageable, clientAccountId);
    }

    @Override
    public Optional<Plate> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Plate> findByOldPlateAndCity(String oldPlate, String city) {
        return repository.findByOldPlateAndCity(oldPlate, city);
    }

}
