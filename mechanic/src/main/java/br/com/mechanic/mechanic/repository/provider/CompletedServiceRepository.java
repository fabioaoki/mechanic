package br.com.mechanic.mechanic.repository.provider;

import br.com.mechanic.mechanic.entity.provider.CompletedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Query("SELECT c.employeeAccountId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.employeeAccountId")
    List<Object[]> countCompletedServicesByEmployeeAccountIdAndOptionalDate(Long providerAccountId, Date startDate, Date endDate);

    @Query("SELECT c.providerServiceId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.providerServiceId")
    List<Object[]> countCompletedServicesByProviderServiceIdAndOptionalDate(Long providerAccountId, Date startDate, Date endDate);

    @Query("SELECT c.vehicleTypeId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.vehicleTypeId")
    List<Object[]> countCompletedServicesByVehicleTypeIdAndOptionalDate(Long providerAccountId, Date startDate, Date endDate);

    @Query("SELECT c.colorId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.colorId")
    List<Object[]> countCompletedServicesByColorIdAndOptionalDate(Long providerAccountId, Date startDate, Date endDate);

    @Query("SELECT c.plateId, COUNT(c) FROM CompletedService c WHERE c.providerAccountId = :providerAccountId AND (c.createDate BETWEEN :startDate AND :endDate OR :startDate IS NULL OR :endDate IS NULL) GROUP BY c.plateId")
    List<Object[]> countCompletedServicesByPlateIdAndOptionalDate(Long providerAccountId, Date startDate, Date endDate);



}

