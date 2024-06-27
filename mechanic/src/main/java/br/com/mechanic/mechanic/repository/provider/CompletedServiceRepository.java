package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompletedServiceRepository extends JpaRepository<CompletedService, Long> {
    Optional<CompletedService> findByProviderAccountId(Long providerAccountId);

    Page<CompletedService> findAllByProviderAccountId(Pageable pageable, Long providerAccountId);

    Page<CompletedService> findByProviderAccountIdAndCreateDateBetween(Long providerAccountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE CompletedService cs SET cs.transactionId = :transactionId WHERE cs.id IN :completedServiceIds")
    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);

    @Query(value = "SELECT ea.name, psi.identifier, vt.name as typeOfCar, COUNT(c) " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "WHERE c.provider_account_id = :providerAccountId " +
            "AND c.create_date BETWEEN :startDate AND :endDate GROUP BY vt.name, ea.name, psi.identifier", nativeQuery = true)
    List<Object[]> countCompletedServicesByEmployeeAccountIdAndOptionalDate(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT ea.name, psi.identifier, vt.name as typeOfCar, COUNT(c) " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "WHERE c.provider_account_id = :providerAccountId " +
            "GROUP BY vt.name, ea.name, psi.identifier", nativeQuery = true)
    List<Object[]> findServiceCountsByProviderAccount(@Param("providerAccountId") Long providerAccountId);

    @Query("SELECT c.providerServiceId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.providerServiceId")
    List<Object[]> countCompletedServicesByProviderServiceIdAndOptionalDate(Long providerAccountId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.vehicleTypeId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.vehicleTypeId")
    List<Object[]> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.colorId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.colorId")
    List<Object[]> countCompletedServicesByColorIdAndOptionalDate(Long providerAccountId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT c.plateId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.plateId")
    List<Object[]> countCompletedServicesByPlateIdAndOptionalDate(Long providerAccountId, LocalDateTime startDate, LocalDateTime endDate);


}

