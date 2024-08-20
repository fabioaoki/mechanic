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

import java.time.LocalDate;
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


    //Relatório de Serviços Concluídos por Período
    //SELECT to_char(c.create_date, 'YYYY-MM') AS month, COUNT(*) AS total_services
    //FROM mechanic.completed_service c
    //WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
    //GROUP BY to_char(c.create_date, 'YYYY-MM')
    //ORDER BY month;
    @Query(value = "SELECT to_char(c.create_date, 'YYYY-MM') AS month, COUNT(*) AS total_services " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE AND c.create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY to_char(c.create_date, 'YYYY-MM') " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> countServicesByMonth(@Param("providerAccountId") Long providerAccountId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    //Relatório de Receita por Tipo de Serviço
    //SELECT psi.identifier, SUM(t.amount) AS total_revenue
    //FROM mechanic.completed_service c
    //INNER JOIN mechanic.transaction t ON c.transaction_id = t.id
    //INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id
    //INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id
    //WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
    //GROUP BY psi.identifier;
    @Query(value = "SELECT psi.identifier as service_type, SUM(t.amount) AS total_revenue " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.transaction t ON c.transaction_id = t.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE AND c.create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY psi.identifier", nativeQuery = true)
    List<Object[]> getTotalRevenueByServiceType(@Param("providerAccountId") Long providerAccountId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);

    //Relatório de Utilização de Equipamento
    @Query(value = "SELECT e.name, " +
            "(SELECT COUNT(eo2.id) FROM mechanic.equipment_out eo2 WHERE eo2.provider_account_id = :providerAccountId AND eo2.reversal = false AND eo2.create_date BETWEEN :startDate AND :endDate) AS usage_count, " +
            "(SELECT COUNT(eo2.id) FROM mechanic.equipment_out eo2 WHERE eo2.provider_account_id = :providerAccountId AND eo2.reversal = false AND eo2.create_date BETWEEN :startDate AND :endDate) * ei.amount AS total_spent " +
            "FROM mechanic.equipment_out eo " +
            "INNER JOIN mechanic.equipment_in ei ON eo.equipment_id = ei.equipment_id " +
            "INNER JOIN mechanic.equipment e ON eo.equipment_id = e.id " +
            "WHERE ei.provider_account_id = :providerAccountId AND eo.reversal = false AND eo.create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.name, ei.amount", nativeQuery = true)
    List<Object[]> getEquipmentUtilization(@Param("providerAccountId") Long providerAccountId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);


    //Relatório de Eficiência dos Funcionários
    //SELECT ea.name, COUNT(c.id) AS services_completed, AVG(c.amount) AS average_service_value
    //FROM mechanic.completed_service c
    //INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id
    //WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
    //GROUP BY ea.name;
    @Query(value = "SELECT ea.name as employee_name, COUNT(c.id) as services_performed, AVG(c.amount) AS average_service_value " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE  AND c.create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY ea.name", nativeQuery = true)
    List<Object[]> getEmployeeEfficiency(@Param("providerAccountId") Long providerAccountId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    //Relatório de Revisões e Manutenções
//SELECT COUNT(*) AS scheduled_revisions,
//       SUM(CASE WHEN r.finish = TRUE THEN 1 ELSE 0 END) AS completed_revisions,
//       SUM(CASE WHEN r.is_deleted = TRUE THEN 1 ELSE 0 END) AS canceled_revisions
//FROM mechanic.revision r
//WHERE r.provider_account_id = :providerAccountId AND r.start_date BETWEEN :startDate AND :endDate;
    @Query(value = "SELECT COUNT(*) AS scheduled_revisions, " +
            "SUM(CASE WHEN r.finish = TRUE THEN 1 ELSE 0 END) AS completed_revisions, " +
            "SUM(CASE WHEN r.is_deleted = TRUE THEN 1 ELSE 0 END) AS canceled_revisions " +
            "FROM mechanic.revision r " +
            "WHERE r.provider_account_id = :providerAccountId AND r.start_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Object[]> getMaintenanceAndRevisionsReport(@Param("providerAccountId") Long providerAccountId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    //Relatório de Comparação de Custos e Receitas
    //SELECT psi.identifier as service_type,
//       SUM(t.workmanship_amount) as total_labor_cost,
//       SUM(t.amount - t.workmanship_amount) as total_equipment_cost,
//       SUM(t.amount) as total_revenue
//FROM mechanic.completed_service c
//INNER JOIN mechanic.transaction t ON c.transaction_id = t.id
//INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id
//INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id
//WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
//GROUP BY psi.identifier;
    @Query(value = "SELECT psi.identifier as service_type, " +
            "SUM(t.workmanship_amount) as total_labor_cost, " +
            "SUM(t.amount - t.workmanship_amount) as total_equipment_cost, " +
            "SUM(t.amount) as total_revenue " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "INNER JOIN mechanic.transaction t ON c.transaction_id = t.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "WHERE c.provider_account_id = :providerAccountId AND r.is_deleted = FALSE AND c.create_date BETWEEN :startDate AND :endDate " +
            "GROUP BY psi.identifier", nativeQuery = true)
    List<Object[]> getCostRevenueComparison(@Param("providerAccountId") Long providerAccountId,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);


    //Relatório de Eficiência de Inventário
    //SELECT e.name as equipment_name, COUNT(eo.id) as usage_frequency, SUM(ei.amount) as total_cost, AVG(ei.amount) as average_cost_per_use
    //FROM mechanic.equipment_out eo
    //INNER JOIN mechanic.equipment_in ei ON eo.equipment_id = ei.equipment_id
    //INNER JOIN mechanic.equipment e ON ei.equipment_id = e.id
    //WHERE eo.provider_account_id = :providerAccountId AND eo.create_date BETWEEN :startDate AND :endDate
    //GROUP BY e.name;
    @Query(value = "SELECT e.name ,  ei.amount , e.id as equipment_id " +
            "FROM mechanic.equipment_in ei " +
            "INNER JOIN mechanic.equipment e on ei.equipment_id = e.id " +
            "WHERE ei.provider_account_id = :providerAccountId AND ei.create_date BETWEEN :startDate AND :endDate ", nativeQuery = true)
    List<Object[]> getInventoryEfficiency(@Param("providerAccountId") Long providerAccountId,
                                          @Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query(value = "select e.name , COUNT(eo.id) , e.id as equipment_id " +
            "FROM mechanic.equipment_out eo " +
            "INNER JOIN mechanic.equipment e ON eo.equipment_id = e.id " +
            "WHERE eo.provider_account_id = :providerAccountId AND eo.reversal = false AND eo.create_date BETWEEN :startDate AND :endDate GROUP BY e.name, e.id", nativeQuery = true)
    List<Object[]> getEquipmentInByProviderAccountId(@Param("providerAccountId") Long providerAccountId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    //Relatório de Custo de Mão de Obra por Serviço
    //SELECT psi.identifier as service_type, SUM(t.workmanship_amount) AS total_labor_cost
//FROM mechanic.completed_service c
//INNER JOIN mechanic.transaction t ON c.transaction_id = t.id
//INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id
//INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id
//WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
//GROUP BY psi.identifier;
    @Query(value = "SELECT psi.identifier as service_type, SUM(t.workmanship_amount) AS total_labor_cost " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.transaction t ON c.transaction_id = t.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate AND r.is_deleted = FALSE " +
            "GROUP BY psi.identifier", nativeQuery = true)
    List<Object[]> getLaborCostByServiceType(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //Relatório de Custos de Equipamentos por Serviço
    //SELECT psi.identifier as service_type, SUM(t.amount - t.workmanship_amount) AS total_equipment_cost
//FROM mechanic.completed_service c
//INNER JOIN mechanic.transaction t ON c.transaction_id = t.id
//INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id
//INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id
//WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
//GROUP BY psi.identifier;
    @Query(value = "SELECT psi.identifier as service_type, SUM(t.amount - t.workmanship_amount) AS total_equipment_cost " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.transaction t ON c.transaction_id = t.id " +
            "INNER JOIN mechanic.provider_service ps ON c.provider_service_id = ps.id " +
            "INNER JOIN mechanic.provider_service_identifier psi ON ps.identifier_id = psi.id " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate AND r.is_deleted = FALSE " +
            "GROUP BY psi.identifier", nativeQuery = true)
    List<Object[]> getEquipmentCostByServiceType(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Relatório de Custo de Mão de Obra por Funcionário
    //SELECT ea.name as employee_name, COUNT(c.id) as services_performed, SUM(t.workmanship_amount) as total_labor_cost
//FROM mechanic.completed_service c
//INNER JOIN mechanic.transaction t ON c.transaction_id = t.id
//INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id
//WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate
//GROUP BY ea.name;
    @Query(value = "SELECT ea.name as employee_name, SUM(t.workmanship_amount) AS total_labor_cost " +
            "FROM mechanic.completed_service c " +
            "INNER JOIN mechanic.employee_account ea ON c.employee_account_id = ea.id " +
            "INNER JOIN mechanic.transaction t ON c.transaction_id = t.id " +
            "INNER JOIN mechanic.revision r ON c.id = r.completed_service_id " +
            "WHERE c.provider_account_id = :providerAccountId AND c.create_date BETWEEN :startDate AND :endDate AND r.is_deleted = FALSE " +
            "GROUP BY ea.name", nativeQuery = true)
    List<Object[]> getLaborCostByEmployee(@Param("providerAccountId") Long providerAccountId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}