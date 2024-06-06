package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PlateRepositoryImpl {
    Optional<Plate> findByMercosulPlate(String plate);

    Plate save(Plate plate);

    Page<Plate> findAll(Pageable pageable);
    Page<Plate> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    Optional<Plate> findById(Long id);

    Optional<Plate> findByOldPlateAndCity(String oldPlate, String city);
}
