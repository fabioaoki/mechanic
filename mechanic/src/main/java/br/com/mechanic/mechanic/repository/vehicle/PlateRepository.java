package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Plate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlateRepository extends JpaRepository<Plate, Long> {
    Optional<Plate> findByMercosulPlate(String mercosulPlate);

    Page<Plate> findAllByClientAccountId(Pageable pageable, Long clientAccountId);

    Optional<Plate> findByOldPlateAndCity(String oldPlate, String city);
}
