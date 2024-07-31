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

    @Query(value = "SELECT c.* FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = :isReversal ", nativeQuery = true)
    Page<CompletedService> findAllByProviderAccountId(@Param("pageable") Pageable pageable, @Param("providerAccountId") Long providerAccountId, @Param("isReversal") boolean isReversal);

    @Query(value = "SELECT c.* FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate AND r.is_deleted = :isReversal ", nativeQuery = true)
    Page<CompletedService> findByProviderAccountIdAndCreateDateBetween(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable, @Param("isReversal") boolean isReversal);

    @Modifying
    @Transactional
    @Query("UPDATE CompletedService cs SET cs.transactionId = :transactionId WHERE cs.id IN :completedServiceIds")
    void setTransactionIds(List<Long> completedServiceIds, Long transactionId);

    @Query(value = "SELECT ea.name, psi.identifier, vt.name as typeOfCar, COUNT(c) " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "AND c.create_date BETWEEN :startDate AND :endDate GROUP BY vt.name, ea.name, psi.identifier", nativeQuery = true)
    List<Object[]> countCompletedServicesByEmployeeAccountIdAndDate(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT ea.name, psi.identifier, vt.name as typeOfCar, COUNT(c) " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "GROUP BY vt.name, ea.name, psi.identifier", nativeQuery = true)
    List<Object[]> findServiceCountsByProviderAccount(@Param("providerAccountId") Long providerAccountId);

    @Query(value = "SELECT psi.identifier, vt.name as typeOfCar, COUNT(c), sum(t.workmanship_amount) as workmanshipAmount, sum(t.amount - t.workmanship_amount) as equipmentValue " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "inner join mechanic.transaction t on c.transaction_id = t.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "GROUP BY vt.name, psi.identifier", nativeQuery = true)
    List<Object[]> countCompletedServicesByProviderServiceId(@Param("providerAccountId") Long providerAccountId);

    @Query(value = "SELECT psi.identifier, vt.name as typeOfCar, COUNT(c), sum(t.workmanship_amount) as workmanshipAmount, sum(t.amount - t.workmanship_amount) as equipmentValue " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "inner join mechanic.transaction t on c.transaction_id = t.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "AND c.create_date BETWEEN :startDate AND :endDate GROUP BY vt.name, psi.identifier", nativeQuery = true)
    List<Object[]> countCompletedServicesByProviderServiceIdAndDate(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query(value = "SELECT psi.identifier, vt.name as typeOfCar, COUNT(c), sum(t.workmanship_amount) as workmanshipAmount, sum(t.amount - t.workmanship_amount) as equipmentValue, to_char(c.create_date, 'YYYY-MM-DD') AS date " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "inner join mechanic.transaction t on c.transaction_id = t.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "AND c.create_date BETWEEN :startDate AND :endDate GROUP BY vt.name, psi.identifier, to_char(c.create_date, 'YYYY-MM-DD')", nativeQuery = true)
    List<Object[]> countCompletedServicesByProviderServiceIdAndDateAndGroupByDate(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT psi.identifier, vt.name as typeOfCar, COUNT(c), to_char(c.create_date, 'YYYY-MM-DD') AS date " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.vehicle_type vt ON c.vehicle_type_id = vt.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE " +
            "AND c.create_date BETWEEN :startDate AND :endDate GROUP BY vt.name, psi.identifier, to_char(c.create_date, 'YYYY-MM-DD')", nativeQuery = true)
    List<Object[]> countCompletedServicesByVehicleTypeIdAndOptionalDate(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE CompletedService cs SET cs.quantity = :partialReversal, cs.lastUpdate = :now WHERE cs.id = :id")
    void partialReversal(@Param("id") Long id, @Param("partialReversal") Long partialReversal, @Param("now") LocalDateTime now);
}