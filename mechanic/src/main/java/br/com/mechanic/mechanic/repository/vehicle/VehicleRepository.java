package br.com.mechanic.mechanic.repository.vehicle;

import br.com.mechanic.mechanic.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlateId(Long plateId);

    Page<Vehicle> findAllByClientAccountId(Pageable pageable, Long clientAccountId);
}
